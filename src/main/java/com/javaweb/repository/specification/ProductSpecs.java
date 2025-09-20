package com.javaweb.repository.specification;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.javaweb.model.builder.ProductBuilder;
import com.javaweb.model.entity.ProductEntity;
import jakarta.persistence.criteria.Predicate;

public class ProductSpecs {
	public static Specification<ProductEntity> withFilter(ProductBuilder product){
		return (root , query , cb) ->{
			List<Predicate> predicates = new ArrayList<>();
			if(product.getName() != null) {
				predicates.add(cb.like(root.get("name"), "%" +  product.getName() + "%"));
			}
			if(product.getPriceFrom() != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("price"), product.getPriceFrom()));
			}
			if(product.getPriceTo() != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("price"), product.getPriceTo()));
			}
			if(product.getRating() !=null) {
				predicates.add(cb.equal(root.get("rating"), product.getRating()));
			}
			if(product.getBrands() != null) {
				predicates.add(root.get("brand").get("id").in(product.getBrands()));
			}
			if(product.getCode() != null) {
				predicates.add(cb.like(root.get("productCode") , "%" + product.getCode() + "%"));
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};	
	}
	public static Specification<ProductEntity> test(String code) {
		return (root , query , cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.like(root.get("productCode") , code));
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
