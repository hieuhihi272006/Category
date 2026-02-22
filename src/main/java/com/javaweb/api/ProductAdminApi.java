package com.javaweb.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.dto.ImportDetailDTO;
import com.javaweb.model.entity.BrandEntity;
import com.javaweb.model.entity.ColorEntity;
import com.javaweb.model.entity.SizeEntity;
import com.javaweb.model.entity.SupplierEntity;
import com.javaweb.model.response.ImportReceiptResponse;
import com.javaweb.model.response.OrderBuyResponse;
import com.javaweb.model.response.ProductResponse;
import com.javaweb.repository.BrandRepository;
import com.javaweb.repository.ColorRepository;
import com.javaweb.repository.SizeRepository;
import com.javaweb.repository.SupplierRepository;
import com.javaweb.service.AdminService;
import com.javaweb.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class ProductAdminApi {
	

	@Autowired
	private ProductService productService;
	@Autowired
	private ColorRepository colorRepository;
	@Autowired
	private SupplierRepository supplierRepository;
	@Autowired
	private SizeRepository sizeRepository;
	@Autowired 
	private BrandRepository brandRepository;
	@Autowired
	private AdminService adminService;




	@PostMapping(value="/size")
	public ResponseEntity<?> createSize(@RequestParam(name = "sizeName" , required = false) String name ){
		if(sizeRepository.existsByName(name)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Size đã tồn tại");
		}
		SizeEntity size = new SizeEntity();
		
		size.setName(name);
		sizeRepository.save(size);
		return ResponseEntity.status(HttpStatus.CREATED).body("Thêm size thành công");
	}
//=================optional
	@GetMapping(value = "/options")
	public ResponseEntity<Map<String,Object>> showOptional(){
		Map<String,Object> result = adminService.getOptional();
		return ResponseEntity.ok(result);
	}
//	===============color
	@PostMapping(value="/color")
	public ResponseEntity<?> creatColor(@RequestParam(name = "colorName" , required = false) String name){
		if(colorRepository.existsByName(name)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Màu đã tồn tại");
		}
		ColorEntity color = new ColorEntity();
		color.setName(name);
		colorRepository.save(color);	
		return ResponseEntity.status(HttpStatus.CREATED).body("Thêm color thành công");
	}
//===================supplier
	@PostMapping(value="/supplier")
	public ResponseEntity<?> createSupplier(@Valid @RequestBody SupplierEntity supplier ,
									BindingResult result){
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(FieldError :: getDefaultMessage).toList();
			return ResponseEntity.badRequest().body(errors);
		}
		
		supplierRepository.save(supplier);
		return ResponseEntity.status(HttpStatus.CREATED).body("Thêm nhà cung cấp thành công");
	}
	
//=============brand
	@PostMapping(value = "/brand")
	public ResponseEntity<?> createBrand(@RequestParam(name = "brandName", required = false) String brandName){
		if(brandRepository.existsByName(brandName)){
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Thương hiệu đã tồn tại");
		}
		BrandEntity result = new BrandEntity();
		result.setName(brandName);
		brandRepository.save(result);
		return ResponseEntity.ok("Tạo thương hiệu thành công");
	}
//	================product
	@PostMapping(value="/product/import")
	public ResponseEntity<?> addProduct(@Valid @RequestBody ImportDetailDTO importDTO , BindingResult result){
		try {
			if(result.hasErrors()) {
				List<String> errors = result.getFieldErrors().stream().map(FieldError :: getDefaultMessage).toList();
				return ResponseEntity.badRequest().body(errors);
			}
			adminService.addProducts(importDTO);
			return ResponseEntity.ok("Thêm sản phẩm thành công");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping(value="/imports/history")
	public ResponseEntity<?> historyImport(@RequestParam(name="startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
											   @RequestParam(name="endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate ){
		if(startDate.isAfter(endDate)) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Ngày bắt đầu không được sau ngày kết thúc");
		}
		List<ImportReceiptResponse> result = adminService.historyImport(startDate,endDate);
		if(result.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Không có sản phẩm nào được nhập vào!");
		}
		return ResponseEntity.ok(result);
	}
	
	@GetMapping(value = "/products")
	public ResponseEntity<?> search(@RequestParam(name="keyword") String keyword){
		List<ProductResponse> products = adminService.search(keyword);
		if(products.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("List null");
		}
		return ResponseEntity.ok(products);
	}
	
	
	@PutMapping(value = "/product/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id){	
		adminService.deleteProduct(id);
		return ResponseEntity.ok("Xóa sản phẩm thành công");
	}
	
	@GetMapping(value = "/product/{id}")
	public ResponseEntity<?> detailProduct(@PathVariable Long id){
		ProductResponse result = productService.detailProduct(id);
		return ResponseEntity.ok(result);	
	}
	
	@PutMapping(value = "/product")
	public ResponseEntity<?> editProduct(@Valid @RequestBody ProductResponse product , BindingResult result){
		try {
			if(result.hasErrors()) {
				List<String> errors = result.getFieldErrors().stream().map(FieldError :: getDefaultMessage).toList();
				return ResponseEntity.badRequest().body(errors);
			}
			adminService.editProduct(product);
			return ResponseEntity.ok("Chỉnh sửa sản phẩm thành công");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping(value = "/order")
	public ResponseEntity<?> historyOrder(@RequestParam(name="startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			   @RequestParam(name="endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
		if(startDate.isAfter(endDate)) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Ngày bắt đầu không được sau ngày kết thúc");
		}
		List<OrderBuyResponse> orderBuyEntities = adminService.historyOrder(startDate, endDate);
		return ResponseEntity.ok(orderBuyEntities);
	}


	
	@GetMapping(value = "/code")
	public ResponseEntity<?> searchByCode(@RequestParam(name = "code") String code){
		ProductResponse product = adminService.searchByCode(code);
		return ResponseEntity.ok(product);
	}
	
	
}
