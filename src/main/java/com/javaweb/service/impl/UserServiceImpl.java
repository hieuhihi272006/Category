package com.javaweb.service.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.javaweb.model.dto.UserDTO;
import com.javaweb.model.dto.UserLoginDTO;
import com.javaweb.model.entity.RoleEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.UserService;
import com.javaweb.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor   
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	
	@Override
	public void createUser(UserDTO userDTO) throws Exception{
		// TODO Auto-generated method stub
		String phoneNumber = userDTO.getPhoneNumber();
		if(userRepository.existsByPhoneNumber(phoneNumber)) {
			throw new Exception("PhoneNumber already exists");
		}
		List<RoleEntity> roles = new ArrayList<>();
		RoleEntity role = new RoleEntity();
		role.setCode("USER");
		role.setId(2);
		role.setName("Người Dùng");
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
		Optional<UserEntity> optionalUser = userRepository.findByPhoneNumber(userLogin.getPhoneNumber());
		if(!optionalUser.isPresent()) {
			throw new Exception("Invalid phone number/password");
		}
		UserEntity user = optionalUser.get();
		if(!passwordEncoder.matches(userLogin.getPassword() , user.getPassword())) {
			throw new Exception("Wrong phone number or password");
		}
		UsernamePasswordAuthenticationToken authen = new UsernamePasswordAuthenticationToken(userLogin.getPhoneNumber() , userLogin.getPassword());
		Authentication authentication = authenticationManager.authenticate(authen);
		return jwtTokenUtil.generateToken(user);
	}

}
