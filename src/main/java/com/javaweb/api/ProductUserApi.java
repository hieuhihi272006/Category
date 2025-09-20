package com.javaweb.api;


import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.javaweb.model.response.FormResponse;
import com.javaweb.model.response.ProductResponse;
import com.javaweb.service.ProductService;


@RestController
@RequestMapping("/api/grocery")
public class ProductUserApi {
	
	@Autowired
	private ProductService productService;

	
	@GetMapping(value="/search")
	public ResponseEntity<FormResponse> search(@RequestParam Map<String,Object> params ,
															@RequestParam(name = "brands" , required = false) List<Integer> brands){
		FormResponse result = productService.search(params, brands);
		return ResponseEntity.ok(result);
	}
	
	@GetMapping(value = "/detailProduct/{id}")
	public ResponseEntity<ProductResponse> detailProduct(@PathVariable Long id){
		ProductResponse result = productService.detailProduct(id);
		return ResponseEntity.ok(result);	
	}
}
