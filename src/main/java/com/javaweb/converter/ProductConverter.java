package com.javaweb.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.javaweb.model.builder.ProductBuilder;
import com.javaweb.model.dto.ImportDetailDTO;
import com.javaweb.model.entity.ImportDetailEntity;
import com.javaweb.model.entity.ImportReceiptEntity;
import com.javaweb.model.entity.ProductEntity;
import com.javaweb.model.entity.ProductVariantEntity;
import com.javaweb.model.entity.SupplierEntity;
import com.javaweb.model.response.ImportDetailResponse;
import com.javaweb.model.response.ProductResponse;
import com.javaweb.model.response.ProductVariantResponse;
import com.javaweb.utils.MapUtil;

@Component
public class ProductConverter {

	@Autowired
	private ModelMapper modelMapper;
	
	
	public static void toPrice(ProductResponse product , ProductEntity it) {
		if(it.getDiscount() != null) {
			if(it.getDiscount() != 0) {
				BigDecimal discount = BigDecimal.valueOf(it.getDiscount()).divide(BigDecimal.valueOf(100));
				BigDecimal cnt = it.getPrice().multiply(discount);
				product.setDiscount_price(it.getPrice().subtract(cnt));
			}
			else {
				product.setDiscount_price(it.getPrice());
			}
		}
	}
	
	public ProductBuilder toProductBuilder(Map<String,Object> params , List<Integer> brands) {
		ProductBuilder result = ProductBuilder.builder()
														.name(MapUtil.getObject(params, "name" , String.class))
														.rating(MapUtil.getObject(params, "rating", Integer.class))
														.priceFrom(MapUtil.getObject(params, "priceFrom", Long.class))
														.priceTo(MapUtil.getObject(params, "priceTo", Long.class))
														.code(MapUtil.getObject(params, "code", String.class))
														.brands(brands)
														.build();
		return result;
	}

	public List<ProductResponse> toProductResponse(List<ProductEntity> listProductEntity){
		List<ProductResponse> listProductResponse = new ArrayList<>();
		for(ProductEntity it : listProductEntity) {
			ProductResponse product = modelMapper.map(it, ProductResponse.class);
			toPrice(product,it);
			listProductResponse.add(product);
		}
		return listProductResponse;
	}
	
	public ProductResponse toProductResponseDetail(ProductEntity productEntity) {
		ProductResponse result = modelMapper.map(productEntity, ProductResponse.class);
		toPrice(result,productEntity);
		List<ProductVariantResponse> productVariantResponses = new ArrayList<>();
		if(productEntity.getProductVariants() != null) {
			for(ProductVariantEntity it : productEntity.getProductVariants()) {
				ProductVariantResponse variant = modelMapper.map(it, ProductVariantResponse.class);
				Map<String , Object> size = new HashMap<>();
				size.put("id", it.getSize() != null ? it.getSize().getId() : null);
				size.put("name", it.getSize() != null ? it.getSize().getName() : null);
				variant.setSizes(size);
		
				Map<String , Object> color = new HashMap<>();
				color.put("id", it.getColor() != null ? it.getColor().getId() : null);
				color.put("name", it.getColor() != null ? it.getColor().getName() : null);
				variant.setColors(color);
				productVariantResponses.add(variant);
				
			}
		}
		
		result.setVariants(productVariantResponses);
		return result;
	}
	
	public ImportDetailEntity toImportDetail(ImportDetailDTO it , ProductVariantEntity variant , SupplierEntity supplier , ImportReceiptEntity importReceipt) {
		ImportDetailEntity importDetail = ImportDetailEntity.builder()
															.price(it.getPrice())
															.quantity(it.getQuantity())
															.supplier(supplier)
															.variant(variant)
															.importReceipt(importReceipt)
															.build();
		return importDetail;
	}
	
	public List<ImportDetailResponse> toImportDetailResponse(ImportReceiptEntity it) {
		List<ImportDetailEntity> importDetailEntities = it.getImportDetails();
		List<ImportDetailResponse> result = new ArrayList<>();
		for (ImportDetailEntity sc : importDetailEntities) {
		    ImportDetailResponse importDetailResponse = modelMapper.map(sc, ImportDetailResponse.class);
		    if (sc.getVariant().getProduct() != null) {
	            importDetailResponse.setName(sc.getVariant().getProduct().getName());
	        }
	        if (sc.getVariant().getColor() != null) {
	            importDetailResponse.setColorName(sc.getVariant().getColor().getName());
	        }
	        if (sc.getVariant().getSize() != null) {
	            importDetailResponse.setSizeName(sc.getVariant().getSize().getName());
	        }
		    if (sc.getSupplier() != null) {
		        importDetailResponse.setSupplierName(sc.getSupplier().getName());
		    }
		    result.add(importDetailResponse);
		}
		return result;	
	}
}
