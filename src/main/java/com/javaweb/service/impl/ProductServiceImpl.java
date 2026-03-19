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
	public FormResponse search(Map<String, Object> params , List<Integer> brands) throws Exception{
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

//	@Override
//	@Transactional
//	public void orderProduct(OrderProductDTO orderProduct, Integer id) {
//		// TODO Auto-generated method stub
//		CartEntity cartEntity = cartRepository.findByUser_Id(Long.valueOf(id))
//		        .orElseGet(() -> {
//		        	CartEntity newCartEntity = new CartEntity(); 
//		        	UserEntity user = userRepository.findById(Long.valueOf(id)).orElseThrow(() -> new UserNotFoundException("Not found user"));
//		        	newCartEntity.setUser(user);
//		        	return newCartEntity;
//		        	});
//		
//		cartRepository.save(cartEntity);
//		ProductVariantEntity productVariantEntity = productVariantRepository.findById(Long.valueOf(orderProduct.getVariantId())).orElseThrow(() -> new ResourceNotFoundException("Not found variant"));
//		Optional<CartItemEntity> cartItem = cartItemRepository.findByCart_IdAndVariant_Id(cartEntity.getId(), orderProduct.getVariantId());
//		
//		if(cartItem.isPresent()) {
//			CartItemEntity item = cartItem.get();
//			item.setQuantity(orderProduct.getQuantity() + item.getQuantity());
//			cartItemRepository.save(item);
//		}
//		else {
//			CartItemEntity cartItemEntity = new CartItemEntity();
//			cartItemEntity.setVariant(productVariantEntity);
//			cartItemEntity.setCart(cartEntity);
//			cartItemEntity.setPrice(orderProduct.getPrice());
//			cartItemEntity.setQuantity(orderProduct.getQuantity());
//			cartItemRepository.save(cartItemEntity);
//		}
//	}

	
	@Override
	@Transactional
	public String test(Long id) throws Exception{
		// TODO Auto-generated method stub	
		ProductVariantEntity p = productVariantRepository.findByIdForUpdate(id);
		
		Thread.sleep(10000);
		
		if(p.getQuantity() <= 0) {
			throw new RuntimeException("Het");
		}
		
		p.setQuantity(p.getQuantity() - 1);
		productVariantRepository.save(p);
		return "ok";
	}



}
