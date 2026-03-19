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
			predicates.add(cb.isFalse(root.get("isDelete")));
			if(product.getName() != null) {
				predicates.add(cb.like(cb.lower(root.get("name")), "%" + product.getName().toLowerCase() + "%"));
			}
			
			if(product.getPriceFrom() != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("maxPrice"), product.getPriceFrom()));
			}
			if(product.getPriceTo() != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("minPrice"), product.getPriceTo()));
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
			if(product.getName() != null) {
				String keyword = product.getName().toLowerCase();
				query.orderBy(
						cb.asc(
								cb.selectCase()
								  .when(cb.like(cb.lower(root.get("name")), keyword), 1)
								  .when(cb.like(cb.lower(root.get("name")), keyword + "%"),2)
								  .when(cb.like(cb.lower(root.get("name")), "% " + keyword + "%"), 3)	
								  .otherwise(4)
								  )
				);
			}
			
			return cb.and(predicates.toArray(new Predicate[0]));
		};	
	}
//	public static Specification<ProductEntity> test(String code) {
//		return (root , query , cb) -> {
//			List<Predicate> predicates = new ArrayList<>();
//			predicates.add(cb.like(root.get("productCode") , code));
//			return cb.and(predicates.toArray(new Predicate[0]));
//		};
//	}
}
