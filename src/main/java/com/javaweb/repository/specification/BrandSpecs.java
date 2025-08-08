package com.javaweb.repository.specification;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import com.javaweb.model.builder.ProductBuilder;
import com.javaweb.model.entity.BrandEntity;
import com.javaweb.model.entity.ProductEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class BrandSpecs {

	public static Specification<BrandEntity> withFilter(ProductBuilder product){
		return (root , query , cb) ->{
			query.distinct(true);
			List<Predicate> predicates = new ArrayList<>();
			if(product.getName() != null) {
				Join<BrandEntity , ProductEntity> productJoin = root.join("listProduct");
				predicates.add(cb.like(productJoin.get("name") , "%" + product.getName() + "%"));
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
