package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.converter.ProductConverter;
import com.javaweb.customException.ResourceNotFoundException;
import com.javaweb.customException.UserNotFoundException;
import com.javaweb.model.builder.ProductBuilder;
import com.javaweb.model.dto.OrderProductDTO;
import com.javaweb.model.entity.CartEntity;
import com.javaweb.model.entity.CartItemEntity;
import com.javaweb.model.entity.ProductEntity;
import com.javaweb.model.entity.ProductVariantEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.model.response.BrandResponse;
import com.javaweb.model.response.FormResponse;
import com.javaweb.model.response.ProductResponse;
import com.javaweb.repository.CartItemRepository;
import com.javaweb.repository.CartRepository;
import com.javaweb.repository.ProductRepository;
import com.javaweb.repository.ProductVariantRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.repository.specification.ProductSpecs;
import com.javaweb.service.ProductService;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductConverter productConverter;
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductVariantRepository productVariantRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartItemRepository cartItemRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public FormResponse search(Map<String, Object> params , List<Integer> brands) {
		// TODO Auto-generated method stub
		ProductBuilder productBuilder = productConverter.toProductBuilder(params,brands);
		List<ProductEntity> listProductEntity = productRepository.findAll(ProductSpecs.withFilter(productBuilder));
		List<ProductResponse> listProduct = productConverter.toProductResponse(listProductEntity);
		
		List<BrandResponse> listBrandResponse = new ArrayList<>();
		HashSet<Integer> set = new HashSet<>();
		if(!listProductEntity.isEmpty()) {
			for(ProductEntity it : listProductEntity) {
				if(it.getBrand() != null) {
					BrandResponse item = modelMapper.map(it.getBrand(), BrandResponse.class);
					if(brands != null) {
						item.setChecked("Checked");
					}
					if(!set.contains(item.getId())) {
						set.add(item.getId());
						listBrandResponse.add(item);
					}
				}
			}
		}
		
  
		FormResponse result = new FormResponse();
		result.setListProduct(listProduct);
		result.setListBrands(listBrandResponse);
		return result;
	}

	@Override
	public ProductResponse detailProduct(Long id) {
		// TODO Auto-generated method stub
		ProductEntity product = productRepository.findById(id).get();
		ProductResponse result = productConverter.toProductResponseDetail(product);
		return result;
	}

	@Override
	@Transactional
	public void orderProduct(OrderProductDTO orderProduct, Integer id) {
		// TODO Auto-generated method stub
		CartEntity cartEntity = cartRepository.findByUser_Id(Long.valueOf(id))
		        .orElseGet(() -> {
		        	CartEntity newCartEntity = new CartEntity(); 
		        	UserEntity user = userRepository.findById(Long.valueOf(id)).orElseThrow(() -> new UserNotFoundException("Not found user"));
		        	newCartEntity.setUser(user);
		        	return newCartEntity;
		        	});
		cartRepository.save(cartEntity);
		
		ProductVariantEntity productVariantEntity = productVariantRepository.findById(Long.valueOf(orderProduct.getVariantId())).orElseThrow(() -> new ResourceNotFoundException("Not found variant"));
		
		Optional<CartItemEntity> cartItem = cartItemRepository.findByCart_IdAndVariant_Id(cartEntity.getId(), orderProduct.getVariantId());
		
		if(cartItem.isPresent()) {
			CartItemEntity item = cartItem.get();
			item.setCart(cartEntity);
			item.setPrice(orderProduct.getPrice());
			item.setQuantity(orderProduct.getQuantity());
			cartItemRepository.save(item);
		}
		else {
			CartItemEntity cartItemEntity = new CartItemEntity();
			cartItemEntity.setVariant(productVariantEntity);
			cartItemEntity.setCart(cartEntity);
			cartItemEntity.setPrice(orderProduct.getPrice());
			cartItemEntity.setQuantity(orderProduct.getQuantity());
			cartItemRepository.save(cartItemEntity);
		}
	}

//	@Transactional
//	@Override
//	public void buyProduct(OrderBuyDTO orderBuyDTO , Integer userId) {
//		// TODO Auto-generated method stub
//		List<CartItemEntity> cartItems = cartItemRepository.findAllById(orderBuyDTO.getIdCartItems());
//		OrderBuyEntity orderBuyEntity = new OrderBuyEntity();
//		UserEntity user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new UserNotFoundException("Not found user"));
//		List<OrderDetailEntity> orderDetailEntities = new ArrayList<>();
//		BigDecimal total = BigDecimal.ZERO;
//		for(CartItemEntity it : cartItems) {
//			OrderDetailEntity orderItem = new OrderDetailEntity();
//			orderItem.setOrder(orderBuyEntity);
//			orderItem.setPrice(it.getPrice());
//			orderItem.setQuantity(it.getQuantity());
//			orderItem.setVariant(it.getVariant());
//			orderDetailEntities.add(orderItem);
//			total = total.add(it.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())));
//		}
//		orderBuyEntity.setOrderDetail(orderDetailEntities);
//		orderBuyEntity.setUser(user);
//		orderBuyEntity.setNote(orderBuyDTO.getNote());
//		orderBuyEntity.setPaymentMethod(orderBuyDTO.getPaymentMethod());
//		orderBuyEntity.setPrice(total);
//		orderBuyEntity.setStatus("Đang xử lý");
//		orderBuyRepository.save(orderBuyEntity);
//		cartItemRepository.deleteAllById(orderBuyDTO.getIdCartItems());
//	}


	
	

}
