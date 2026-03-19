package com.javaweb.service.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.javaweb.model.dto.UserDTO;
import com.javaweb.model.dto.UserLoginDTO;
import com.javaweb.model.entity.CartEntity;
import com.javaweb.model.entity.CartItemEntity;
import com.javaweb.model.entity.OrderBuyEntity;
import com.javaweb.model.entity.RoleEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.model.response.CartResponse;
import com.javaweb.model.response.InformationUserResponse;
import com.javaweb.model.response.OrderBuyResponse;
import com.javaweb.repository.*;
import com.javaweb.service.UserService;
import com.javaweb.utils.JwtTokenUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor   
public class UserServiceImpl implements UserService{

   
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private RoleRepository roleripository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OrderBuyRepository buyRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartItemRepository cartItemRepository;
	
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;

   
	
	@Override
	@Transactional
	public void register(UserDTO userDTO) throws Exception{
		// TODO Auto-generated method stub
		String phoneNumber = userDTO.getPhoneNumber();
		if(userRepository.existsByPhoneNumber(phoneNumber)) {
			throw new Exception("PhoneNumber already exists");
		}
		List<RoleEntity> roles = new ArrayList<>();
		RoleEntity role = roleripository.findById(2).get();
		roles.add(role);
		UserEntity newUser = UserEntity.builder()
								.name(userDTO.getName())
								.address(userDTO.getAddress())	
								.email(userDTO.getEmail())
								.phoneNumber(userDTO.getPhoneNumber())
								.status(1)
								.password(userDTO.getPassword())
								.build();
		newUser.setRoles(roles);
		String encoderPassword = passwordEncoder.encode(userDTO.getPassword());
		newUser.setPassword(encoderPassword);
		userRepository.save(newUser);
	}
	
	@Override
	public String loginUser(UserLoginDTO userLogin) throws Exception{
		// TODO Auto-generated method stub
//		Optional<UserEntity> optionalUser = userRepository.findByPhoneNumber(userLogin.getPhoneNumber());
//		if(!optionalUser.isPresent()) {
//			throw new Exception("Invalid phone number/password");
//		}
//		UserEntity user = optionalUser.get();
//		if(!passwordEncoder.matches(userLogin.getPassword() , user.getPassword())) {
//			throw new Exception("Wrong phone number or password");
//		}
//		UsernamePasswordAuthenticationToken authen = UsernamePasswordAuthenticationToken.unauthenticated(userLogin.getPhoneNumber() , userLogin.getPassword());
//		Authentication authentication = authenticationManager.authenticate(authen);
		UserDetails user = (UserDetails) authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(userLogin.getPhoneNumber(), userLogin.getPassword())).getPrincipal();
		return jwtTokenUtil.generateToken(user);
	}

	@Override
	public InformationUserResponse getProfile(UserEntity user) {
		// TODO Auto-generated method stub
		InformationUserResponse infomation = modelMapper.map(user,InformationUserResponse.class );
		return infomation;
	}

	@Override
	public List<OrderBuyResponse> getOrder(UserEntity user) {
		// TODO Auto-generated method stub
		
		List<OrderBuyEntity> orders = buyRepository.findAllByUser_Id(user.getId());
		List<OrderBuyResponse> orderBuyResponses = new ArrayList<>();
		
		for(OrderBuyEntity it : orders) {
			OrderBuyResponse item = modelMapper.map(it, OrderBuyResponse.class);
			orderBuyResponses.add(item);
		}
		return orderBuyResponses;
	}

	@Override
	@Transactional
	public List<CartResponse> getCard(UserEntity user) {
		// TODO Auto-generated method stub
		Optional< CartEntity> cartEntity = cartRepository.findByUser_Id(user.getId());
		if(cartEntity.isPresent()) {
			List<CartResponse> cartResponse = new ArrayList<>();
			for(CartItemEntity it : cartEntity.get().getCartItems()) {
				CartResponse item = new CartResponse();
				item.setId(it.getId());
				item.setName(it.getVariant().getProduct().getName());
				item.setImage(it.getVariant().getProduct().getImage());
				item.setQuantity(it.getQuantity());
				item.setPrice(it.getPrice());
				item.setIdProduct(it.getVariant().getProduct().getId());
				if(it.getVariant().getColor() != null) {
					item.setColor(it.getVariant().getColor().getName());
				}
				if(it.getVariant().getSize() != null) {
					item.setSize(it.getVariant().getSize().getName());
				}
				
				cartResponse.add(item);
			}
			return cartResponse;
		}
		return null;
	}

	@Override
	@Transactional
	public void deleteCart(Long id) {
		// TODO Auto-generated method stub
		cartItemRepository.deleteById(id);
	}
	
}
