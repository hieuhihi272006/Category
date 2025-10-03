package com.javaweb.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.javaweb.converter.BrandConverter;
import com.javaweb.converter.ProductConverter;
import com.javaweb.model.builder.ProductBuilder;
import com.javaweb.model.dto.ImportDetailDTO;
import com.javaweb.model.entity.BrandEntity;
import com.javaweb.model.entity.ColorEntity;
import com.javaweb.model.entity.ImportReceiptEntity;
import com.javaweb.model.entity.ProductEntity;
import com.javaweb.model.entity.ProductVariantEntity;
import com.javaweb.model.entity.SizeEntity;
import com.javaweb.model.entity.SupplierEntity;
import com.javaweb.model.response.BrandResponse;
import com.javaweb.model.response.FormResponse;
import com.javaweb.model.response.ImportDetailResponse;
import com.javaweb.model.response.ProductResponse;
import com.javaweb.model.response.ProductVariantResponse;
import com.javaweb.repository.*;
import com.javaweb.service.ProductService;
import com.javaweb.utils.MapUtil;
import jakarta.transaction.Transactional;
import com.javaweb.repository.specification.BrandSpecs;
import com.javaweb.repository.specification.ProductSpecs;
import com.javaweb.repository.SupplierRepository;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductConverter productConverter;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private BrandRepository brandRepository;
	@Autowired
	private BrandConverter brandConverter;
	@Autowired
	private ColorRepository colorRepository;
	@Autowired
	private SupplierRepository supplierRepository;
	@Autowired
	private ImportReceiptRepository importReceiptRepository;
	@Autowired
	private SizeRepository sizeRepository;
	@Autowired
	private ImportDetailRepository importDetailRepository;
	@Autowired
	private ProductVariantRepository productVariantRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public FormResponse search(Map<String, Object> params , List<Integer> brands) {
		// TODO Auto-generated method stub
		ProductBuilder productBuilder = productConverter.toProductBuilder(params,brands);
		List<ProductEntity> listProductEntity = productRepository.findAll(ProductSpecs.withFilter(productBuilder));
		List<ProductResponse> listProduct = productConverter.toProductResponse(listProductEntity);
		List<BrandEntity> listBrandEntity = brandRepository.findAll(BrandSpecs.withFilter(productBuilder));
		List<BrandResponse> listBrandResponse = brandConverter.toBrandResponse(listBrandEntity, brands);
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
	public Map<String, Object> showSize(String code) {
		Map<String,Object> result = new HashMap<>();
		Map<Integer , Object> size = new HashMap<>();
		try {
			ProductEntity product = productRepository.findByProductCode(code).orElseThrow(() -> new RuntimeException());
//			ProductEntity product = productRepository.findOne(ProductSpecs.test(code)).orElseThrow(() -> new RuntimeException());
			// TODO Auto-generated method stub
			result.put("name", product.getName());
			if(product.getProductVariants().isEmpty()) {
				throw new RuntimeException("");
			}
			for (ProductVariantEntity it : product.getProductVariants()) {
		        if (it.getSize() != null) {
		               size.put(it.getSize().getId(), it.getSize().getName());  
		        }
		    }
		}catch(Exception e) {
			for(SizeEntity it : sizeRepository.findAll()) {
				size.put(it.getId(),it.getName());
			}
		}
		result.put("size", size);
		return result;
	}

	@Override
	public Map<String, Object> showColorAndSupplierAndBrand() {
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<>();
		Map<Integer , String> color = new HashMap<>();
		List<ColorEntity> listColor = colorRepository.findAll();
		listColor.forEach(it -> color.put(it.getId(), it.getName()));
		Map<Integer,String> supplier = new HashMap<>();
		supplierRepository.findAll().forEach(it -> supplier.put(it.getId(), it.getName()));
		Map<Integer,String> brand = new HashMap<>();
		brandRepository.findAll().forEach(it -> brand.put(it.getId(), it.getName()));
		result.put("brand",brand);
		result.put("color", color);
		result.put("supplier", supplier);
		return result;
	}

	@Override
	@Transactional
	public void addProducts(List<ImportDetailDTO> importDTOs) {
		// TODO Auto-generated method stub
		ImportReceiptEntity importReceipt = new ImportReceiptEntity();
		importReceiptRepository.save(importReceipt);
		for(ImportDetailDTO it : importDTOs) {
			SupplierEntity supplier = supplierRepository.findById(it.getSupplierId());
			Optional<ProductEntity> product = productRepository.findByProductCode(it.getProductCode());
			if(product.isPresent()) {
				List<ProductVariantEntity> variants = product.get().getProductVariants();
				boolean check = false;
				ProductVariantEntity variant = new ProductVariantEntity();
				for(ProductVariantEntity sc : variants) {
					Object obj1 = sc.getSize() != null ? sc.getSize().getId() : null;
					Object obj2 = sc.getColor() != null ? sc.getColor().getId() : null;
					if (Objects.equals(obj1, it.getSizeId()) && Objects.equals(obj2, it.getColorId())) {
					    sc.setQuantity(sc.getQuantity() + it.getQuantity());
					    check = true;
					    variant = sc;
					}

				}
				if(!check) {
					ProductVariantEntity newVariant = new ProductVariantEntity();
					newVariant.setPrice(it.getPrice());
					newVariant.setQuantity(it.getQuantity());
					ColorEntity color = colorRepository.findById(it.getColorId());
					newVariant.setColor(color);
					SizeEntity size = sizeRepository.findById(it.getSizeId());
					newVariant.setSize(size);
					newVariant.setProduct(product.get());
					productVariantRepository.save(newVariant);
					variants.add(newVariant);
					variant = newVariant;
				}
				importDetailRepository.save(productConverter.toImportDetail(it,variant,supplier,importReceipt));
				product.get().setProductVariants(variants);
				product.get().setQuantity(it.getQuantity() + product.get().getQuantity());
				productRepository.save(product.get());
			}
			else {
				ProductEntity newProduct = ProductEntity.builder()
														.name(it.getName())
														.price(it.getPrice())
														.productCode(it.getProductCode())
														.quantity(it.getQuantity())
														.discount(0)
														.build();
				productRepository.save(newProduct);
				ProductVariantEntity variant = ProductVariantEntity.builder()
																   .color(colorRepository.findById(it.getColorId()))
																   .size(sizeRepository.findById(it.getSizeId()))
																   .price(it.getPrice())
																   .quantity(it.getQuantity())
																   .product(newProduct)
																   .build();
				productVariantRepository.save(variant);
				importDetailRepository.save(productConverter.toImportDetail(it,variant,supplier,importReceipt));
			}
		}
		
	}

	@Override
	public List<ImportDetailResponse> historyImport(LocalDate startDate,LocalDate endDate) {
		// TODO Auto-generated method stub
		List<ImportReceiptEntity> importReceipts = importReceiptRepository.findByCreatedAtBetween(startDate, endDate);
		List<ImportDetailResponse> result = new ArrayList<>();
		for(ImportReceiptEntity it : importReceipts) {
			List<ImportDetailResponse> importDetailResponse = productConverter.toImportDetailResponse(it);
			result.addAll(importDetailResponse);
		}
		return result;
	}

	@Override
	public void editProduct(ProductResponse product) {
		// TODO Auto-generated method stub
		ProductEntity newProduct = modelMapper.map(product, ProductEntity.class);
		List<ProductVariantEntity> listVariant = new ArrayList<>();
		for(ProductVariantResponse it : product.getVariants()) {
			ProductVariantEntity variant = modelMapper.map(it, ProductVariantEntity.class);
			variant.setPrice(it.getPrice());
			variant.setQuantity(it.getQuantity());
			
			Integer idSize = MapUtil.getObject(it.getSizes(),"id",Integer.class);
			SizeEntity size = sizeRepository.findById(idSize);
			
			Integer idColor = MapUtil.getObject(it.getColors(),"id",Integer.class);
			ColorEntity color = colorRepository.findById(idColor);

			variant.setColor(color);
			variant.setSize(size);
			variant.setProduct(newProduct);
			listVariant.add(variant);
		}
		BrandEntity brand = brandRepository.findById(product.getBrandId());
		newProduct.setProductVariants(listVariant);
		newProduct.setBrand(brand);
		productRepository.save(newProduct);
	}

}
