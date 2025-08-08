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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.javaweb.model.dto.ImportDetailDTO;
import com.javaweb.model.entity.BrandEntity;
import com.javaweb.model.entity.ColorEntity;
import com.javaweb.model.entity.SizeEntity;
import com.javaweb.model.entity.SupplierEntity;
import com.javaweb.model.response.FormResponse;
import com.javaweb.model.response.ImportDetailResponse;
import com.javaweb.model.response.ProductResponse;
import com.javaweb.repository.*;
import com.javaweb.service.ProductService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/admin_grocary")
public class ProductAdminApi {
	
	@Autowired
    private ProductRepository productRepository;
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

	@GetMapping(value = "/show_size")
	public ResponseEntity<Map<String,Object>> showSize(@RequestParam(name = "code") String code){
		Map<String,Object> result = productService.showSize(code);
		return ResponseEntity.ok(result);
	}

	@GetMapping(value = "/show_optional")
	public ResponseEntity<Map<String,Object>> showColorAndSupplierAndBrand(){
		Map<String,Object> result = productService.showColorAndSupplierAndBrand();
		return ResponseEntity.ok(result);
	}
	
	@PostMapping(value="/create_color")
	public ResponseEntity<?> creatColor(@RequestParam(name = "colorName" , required = false) String name){
		if(colorRepository.existsByName(name)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Màu đã tồn tại");
		}
		ColorEntity color = new ColorEntity();
		color.setName(name);
		colorRepository.save(color);	
		return ResponseEntity.ok("");
	}
	
	@PostMapping(value="/create_supplier")
	public ResponseEntity<?> createSupplier(@Valid @RequestBody SupplierEntity supplier ,
									BindingResult result){
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
			return ResponseEntity.badRequest().body(errors);
		}

		supplierRepository.save(supplier);
		return ResponseEntity.status(HttpStatus.CREATED).body("Thêm nhà cung cấp thành công");
	}
	
	@PostMapping(value="/create_size")
	public ResponseEntity<?> createSize(@RequestParam(name = "sizeName" , required = false) String name ){
		if(sizeRepository.existsByName(name)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Size đã tông tại");
		}
		SizeEntity size = new SizeEntity();
		size.setName(name);
		sizeRepository.save(size);
		return ResponseEntity.status(HttpStatus.CREATED).body("Thêm size thành công");
	}
	
	@PostMapping(value="/add_products")
	public ResponseEntity<?> addProducts(@Valid @RequestBody List<ImportDetailDTO> importDTO , BindingResult result){
		try {
			if(result.hasErrors()) {
				List<String> errors = result.getFieldErrors().stream().map(FieldError :: getDefaultMessage).toList();
				return ResponseEntity.badRequest().body(errors);
			}
			productService.addProducts(importDTO);
			return ResponseEntity.ok("");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping(value="/history_import")
	public ResponseEntity<?> historyImport(@RequestParam(name="startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
											   @RequestParam(name="endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate ){
		if(startDate.isAfter(endDate)) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Ngày bắt đầu không được sau ngày kết thúc");
		}
		List<ImportDetailResponse> result = productService.historyImport(startDate,endDate);
		if(result.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Không có sản phẩm nào được nhập vào!");
		}
		return ResponseEntity.ok(result);
	}
	
	@PostMapping(value = "/creat_brand")
	public ResponseEntity<?> createBrand(@RequestParam(name = "brandName", required = false) String brandName){
		if(brandRepository.existsByName(brandName)){
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Thương hiệu đã tồn tại");
		}
		BrandEntity result = new BrandEntity();
		result.setName(brandName);
		brandRepository.save(result);
		return ResponseEntity.ok("Tạo thương hiệu thành công");
	}
	
	@GetMapping(value = "/search")
	public ResponseEntity<?> search(@RequestParam Map<String,Object> map){
		FormResponse form = productService.search(map, null);
		List<ProductResponse> result = form.getListProduct();
		if(result.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Không tìm thấy sản phẩm");
		}
		return ResponseEntity.ok(result);
	}
	
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id){
		productRepository.deleteById(id);
		return ResponseEntity.ok("Xóa sản phẩm thành công");
	}
	
	@GetMapping(value = "/detail_product/{id}")
	public ResponseEntity<?> detailProduct(@PathVariable Long id){
		ProductResponse result = productService.detailProduct(id);
		return ResponseEntity.ok(result);	
	}
	
	@PostMapping(value = "/edit_product")
	public ResponseEntity<?> editProduct(@Valid @RequestBody ProductResponse product , BindingResult result){
		try {
			if(result.hasErrors()) {
				List<String> errors = result.getFieldErrors().stream().map(FieldError :: getDefaultMessage).toList();
				return ResponseEntity.badRequest().body(errors);
			}
			productService.editProduct(product);
			return ResponseEntity.ok("Chỉnh sửa sản phẩm thành công");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
