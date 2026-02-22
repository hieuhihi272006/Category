package com.javaweb.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.dto.OrderProductDTO;
import com.javaweb.model.entity.BannerEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.model.response.FormResponse;
import com.javaweb.model.response.ProductResponse;
import com.javaweb.repository.BannerRepository;
import com.javaweb.service.ProductService;


@RestController
@RequestMapping("/api/grocery")
public class ProductUserApi {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private BannerRepository bannerRepository;

	
	@GetMapping(value="/products")
	public ResponseEntity<FormResponse> search(@RequestParam Map<String,Object> params ,
															@RequestParam(name = "brands" , required = false) List<Integer> brands){
		FormResponse result = productService.search(params, brands);
		return ResponseEntity.ok(result);	
	}
	
	@GetMapping(value = "/product/{id}")	
	public ResponseEntity<ProductResponse> detailProduct(@PathVariable Long id){
		ProductResponse result = productService.detailProduct(id);
		return ResponseEntity.ok(result);	
	}
	
	@GetMapping(value="/banners")
	public ResponseEntity<?> showBanner(){
		List<BannerEntity> listBanner = bannerRepository.findAll();
		if(listBanner.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Banner empty");
		}
		return ResponseEntity.ok(listBanner);
	}
	
	@PostMapping(value = "/cart")
	public ResponseEntity<?> orderProduct(@RequestBody OrderProductDTO orderProduct ,
											@AuthenticationPrincipal UserEntity user){
		productService.orderProduct(orderProduct, user.getId());
		return ResponseEntity.ok("Them san pham vao gio hang thanh cong");
	}
	

//	@GetMapping(value = "/hello")
//	@RolesAllowed("ADMIN")
//	public ResponseEntity<?> hello(@RequestParam(name = "test") String test , @NotNull HttpServletRequest request){
//		System.out.print(test);
//		return ResponseEntity.ok("hello");
//	}
	
	@GetMapping(value = "/test/{id}")
	public ResponseEntity<?> test(@PathVariable Long id){
		String result = productService.test(id);
		return ResponseEntity.ok(result);
	}
	
	
}
