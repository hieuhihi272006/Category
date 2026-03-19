package com.javaweb.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaweb.converter.ProductConverter;
import com.javaweb.model.builder.ProductBuilder;
import com.javaweb.model.dto.ImportDetailDTO;
import com.javaweb.model.dto.ImportVariantDTO;
import com.javaweb.model.entity.BrandEntity;
import com.javaweb.model.entity.ColorEntity;
import com.javaweb.model.entity.ImportDetailEntity;
import com.javaweb.model.entity.ImportReceiptEntity;
import com.javaweb.model.entity.OrderBuyEntity;
import com.javaweb.model.entity.ProductEntity;
import com.javaweb.model.entity.ProductVariantEntity;
import com.javaweb.model.entity.SizeEntity;
import com.javaweb.model.response.ImportDetailResponse;
import com.javaweb.model.response.ImportReceiptResponse;
import com.javaweb.model.response.OrderBuyResponse;
import com.javaweb.model.response.ProductResponse;
import com.javaweb.model.response.ProductVariantResponse;
import com.javaweb.repository.*;
import com.javaweb.repository.specification.ProductSpecs;
import com.javaweb.service.AdminService;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class AdminServiceImpl implements AdminService{


	
	@Autowired
	private ProductConverter productConverter;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private BrandRepository brandRepository;
	@Autowired
	private ColorRepository colorRepository;
	@Autowired
	private SupplierRepository supplierRepository;
	@Autowired
	private ImportReceiptRepository importReceiptRepository;
	@Autowired
	private SizeRepository sizeRepository;
	@Autowired
	private ProductVariantRepository productVariantRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private OrderBuyRepository orderBuyRepository;
	@Autowired
	private EntityManager entityManager;
  
	
	
	@Override
	public Map<String, Object> getOptional() {
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<>();
		Map<Integer , String> color = new HashMap<>();
		List<ColorEntity> listColor = colorRepository.findAll();
		listColor.forEach(it -> color.put(it.getId(), it.getName()));
		Map<Integer,String> supplier = new HashMap<>();
		supplierRepository.findAll().forEach(it -> supplier.put(it.getId(), it.getName()));
		Map<Integer,String> brand = new HashMap<>();
		brandRepository.findAll().forEach(it -> brand.put(it.getId(), it.getName()));
		Map<Integer , String> size = new HashMap<>();
		sizeRepository.findAll().forEach(it -> size.put(it.getId(), it.getName()));
		result.put("brand",brand);
		result.put("color", color);
		result.put("supplier", supplier);
		result.put("size", size);
		return result;
	}
	
	public static String generateCode(int length) {
		String charater = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder code = new StringBuilder(length);
		Random random = new Random();
		for(int i = 0 ; i < length ; i++) {
			code.append(charater.charAt(random.nextInt(charater.length())));
		}
		return code.toString();
	}
	
	@Override
	@Transactional
	public void addProducts(ImportDetailDTO importDTO) {
		// TODO Auto-generated method stub
		
		ImportReceiptEntity importReceiptEntity = new ImportReceiptEntity();
		List<ImportDetailEntity> importDetailEntity = new ArrayList<>(); 
		Optional<ProductEntity> product = productRepository.findByProductCodeAndIsDelete(importDTO.getProductCode() , false);
//		============ product not exits
		if(!product.isPresent()) {
			ProductEntity newProduct = new ProductEntity();
			newProduct.setProductCode(importDTO.getProductCode());
			newProduct.setName(importDTO.getName());
//			newProduct.setPrice(0L);
			productRepository.save(newProduct);
			if(importDTO.getImportVariant() != null) {
				for(ImportVariantDTO it : importDTO.getImportVariant()) {
					
					ProductVariantEntity newVariant =  ProductVariantEntity.builder()
														.color(colorRepository.findById(it.getColorId()))
														.size(sizeRepository.findById(it.getSizeId()))
														.quantity(it.getQuantity() != null ? it.getQuantity() : 0)
														.price(it.getPrice() != null ? it.getPrice() : 0)
														.product(newProduct)
														.build();
					ImportDetailEntity importDetail =  ImportDetailEntity.builder()
														.importReceipt(importReceiptEntity)
														.variant(newVariant)
//														.supplier(supplierRepository.findById(importDTO.getSupplierId()))
														.quantity(it.getQuantity())
														.price(it.getPrice())
														.build();
					importDetailEntity.add(importDetail);
				}
			}
		}
//		============ Import old product
		else {
			if(importDTO.getImportVariant() != null) {
				for(ImportVariantDTO it : importDTO.getImportVariant()) {
					
					ProductVariantEntity variantUpdate = productVariantRepository.findBySize_IdAndColor_IdAndProduct_ProductCode(it.getSizeId(),it.getColorId(),product.get().getProductCode())
							.orElseGet(() -> {
								ProductVariantEntity newVariant =  ProductVariantEntity.builder()
										.color(colorRepository.findById(it.getColorId()))
										.size(sizeRepository.findById(it.getSizeId()))
										.quantity(0)
										.price(it.getPrice() != null ? it.getPrice() : 0)
										.product(product.get())
										.build();
								return newVariant;
							});
					variantUpdate.setQuantity(variantUpdate.getQuantity() + it.getQuantity());
	
					ImportDetailEntity importDetail =  ImportDetailEntity.builder()
							.importReceipt(importReceiptEntity)
							.variant(variantUpdate)
							.quantity(it.getQuantity())
							.price(it.getPrice())
							.build();
					importDetailEntity.add(importDetail);
				}
			}
			
		}
		importReceiptEntity.setImportDetails(importDetailEntity);
		importReceiptEntity.setCode("ORD-"+generateCode(8));
		if(importDTO.getSupplierId() != null) {
			importReceiptEntity.setSupplier(supplierRepository.findById(importDTO.getSupplierId()));
		}
		importReceiptRepository.save(importReceiptEntity);
		
	}
	
	@Override
	public List<ImportReceiptResponse> historyImport(LocalDate startDate,LocalDate endDate) {
		// TODO Auto-generated method stub
		List<ImportReceiptEntity> importReceipts = importReceiptRepository.findByCreatedAtBetween(startDate, endDate);
		List<ImportReceiptResponse> importReceiptRespone = new ArrayList<>();
		
		for(ImportReceiptEntity it : importReceipts) {
			List<ImportDetailResponse> importDetailResponse = productConverter.toImportDetailResponse(it);
			Long totalPrice = 0L;
			Integer quantity = 0;
			for(ImportDetailResponse sc : importDetailResponse) {
				totalPrice += sc.getPrice()*sc.getQuantity();
				quantity += sc.getQuantity();
			}
			
			ImportReceiptResponse item =  ImportReceiptResponse.builder()
					.code(it.getCode())
					.date(it.getCreatedAt())
					.supplierName(it.getSupplier() != null ? it.getSupplier().getName() : "")
					.name(it.getImportDetails().get(0).getVariant().getProduct().getName())
					.quantity(quantity)
					.totalPrice(totalPrice)
					.listImportDetail(importDetailResponse)
					.build();
			
			importReceiptRespone.add(item);
		}
		return importReceiptRespone;
	}
	
	
	@Override
	public void editProduct(ProductResponse product) {
		// TODO Auto-generated method stub

		ProductEntity newProduct = modelMapper.map(product, ProductEntity.class);

		BrandEntity brand = new BrandEntity();
		brand.setId(product.getBrandId());
		brand.setName(product.getBrandName());
		newProduct.setBrand(brand);
		
		Integer quantity = 0;
		Long minPrice = Long.MAX_VALUE ;
		Long maxPrice = Long.MIN_VALUE;
		Long minPriceOriginal = 0L;
		Integer discount = 0;
		
		List<ProductVariantEntity> listVariant = new ArrayList<>();
		for(ProductVariantResponse it : product.getVariants()) {
			ProductVariantEntity variant = modelMapper.map(it, ProductVariantEntity.class);
			quantity += variant.getQuantity();
			
			Long price = it.getPrice() - it.getPrice() * it.getDiscount() / 100;
			if(price < minPrice) {
				minPrice = price;
				minPriceOriginal = it.getPrice();
				discount = it.getDiscount();
			}
			
			if(price > maxPrice) {
				maxPrice = price;
			}
			
			
			if(it.getSizes().get("id") != null) {
				Integer sizeId = Integer.valueOf(it.getSizes().get("id").toString());
				SizeEntity size = entityManager.getReference(SizeEntity.class, sizeId);
				size.setName(it.getSizes().get("name").toString());
				variant.setSize(size);
			}

			if(it.getColors().get("id") != null) {
				Integer colorId = Integer.valueOf(it.getColors().get("id").toString());
				ColorEntity color = entityManager.getReference(ColorEntity.class, colorId);
				color.setName(it.getColors().get("name").toString());
				variant.setColor(color);
			}
			listVariant.add(variant);
			variant.setProduct(newProduct);
		}
		
		newProduct.setProductVariants(listVariant);
		newProduct.setTotalQuantity(quantity);
		newProduct.setMinPrice(minPrice);
		newProduct.setMaxPrice(maxPrice);
		newProduct.setDiscountPercent(discount);
		newProduct.setOriginalPrice(minPriceOriginal);
		
		productRepository.save(newProduct);
		
		
	}
	

//	@Override
//	public List<OrderBuyResponse> historyOrder(LocalDate startDate, LocalDate endDate) {
//		// TODO Auto-generated method stub
//		List<OrderBuyEntity> orderBuyEntities = orderBuyRepository.findByCreatedAtBetween(startDate, endDate);
//		List<OrderBuyResponse> buyResponses = new ArrayList<>();
//		for(OrderBuyEntity it : orderBuyEntities){
//			OrderBuyResponse item = modelMapper.map(it, OrderBuyResponse.class);
//			item.setCode("MD"+String.valueOf(it.getId()));
//			item.setNameUser(it.getUser().getName());
//			item.setAddress(it.getUser().getAddress());
//			item.setNumberPhone(it.getUser().getPhoneNumber());
//			buyResponses.add(item);
//		}
//		return buyResponses;
//	}

	@Transactional
	@Override
	public List<ProductResponse> search(String keyword) {
		// TODO Auto-generated method stub
		List<ProductResponse> productResponses = new ArrayList<>();
		if(keyword == null || keyword.trim().isEmpty()) {
			List<ProductEntity> productsByAll = productRepository.findAll();
			productResponses = productConverter.toProductResponse(productsByAll);
		}
		else {
			ProductEntity productsByCode = productRepository.findByProductCode(keyword).orElse(null);
			if(productsByCode == null) {
				List<ProductEntity> productsByName = productRepository.findByName(keyword);
				productResponses = productConverter.toProductResponse(productsByName);
			}
			else {
				List<ProductEntity> listProductByCode = new ArrayList<>();
				listProductByCode.add(productsByCode);
				productResponses = productConverter.toProductResponse(listProductByCode);
			}
		}
		return productResponses;
	}

	@Override
	@Transactional
	public void statusProduct(Long id, boolean status) {
		// TODO Auto-generated method stub
		ProductEntity product = productRepository.findById(id).get();
		product.setDelete(status);
		productRepository.save(product);
	}

	@Override
	public ProductResponse searchByCode(String code) {
		// TODO Auto-generated method stub
		ProductEntity product = productRepository.findByProductCode(code).orElse(null);
		ProductResponse productResponse = new ProductResponse();
		if(product != null) {
			productResponse = modelMapper.map(product,ProductResponse.class);
		}
		else {
			productResponse.setName("");
		}
		return productResponse;
	}
	

}
