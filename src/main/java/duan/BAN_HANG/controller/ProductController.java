package duan.BAN_HANG.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import duan.BAN_HANG.helper.ApiResponse;
import duan.BAN_HANG.reponseDTO.PageResponse;
import duan.BAN_HANG.reponseDTO.ProductReponseDTO;
import duan.BAN_HANG.requestDTO.ProductRequestDTO;
import duan.BAN_HANG.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public ResponseEntity<?> getAllProducts(String searchKeyword, Pageable pageable) {
		Page<ProductReponseDTO> products = this.productService.getAllProducts(searchKeyword, pageable);
		return ApiResponse.success(PageResponse.from(products), "Lấy danh sách sản phẩm thành công");
	}

//	@GetMapping
//	public ResponseEntity<?> getAllProducts(ProductFilterDTO productFilterDTO, Pageable pageable) {
//		Page<ProductReponseDTO> products = this.productService.getAllProducts1(productFilterDTO, pageable);
//		return ApiResponse.success(PageResponse.from(products), "Lấy danh sách sản phẩm thành công");
//	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createProduct(@Valid @RequestPart("product") ProductRequestDTO productDTO,
			@RequestPart("file") List<MultipartFile> files) throws IOException {
		ProductReponseDTO savedProduct = this.productService.createProduct(productDTO, files);
		return ApiResponse.success(savedProduct, "Tạo sản phẩm thành công");
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@Valid @RequestPart("product") ProductRequestDTO productDTO,
			@RequestPart(value = "file", required = false) List<MultipartFile> files,
			@RequestParam(value = "deleteImageIds", required = false) List<Long> deleteImageIds, @PathVariable Long id)
			throws IOException {
		this.productService.updateProduct(productDTO, files, deleteImageIds, id);
		return ApiResponse.success("Cập nhật sản phẩm thành công");
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getProductById(@PathVariable Long id) {
		ProductReponseDTO productReponseDTO = this.productService.getProductById(id);
		return ApiResponse.success(productReponseDTO, "Lấy sản phẩm thành công");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		this.productService.deleteProduct(id);
		return ApiResponse.success(null, "Xóa sản phẩm thành công");
	}

	@GetMapping("/productPage/{categoryId}")
	public ResponseEntity<?> getProductByCategory(@PathVariable Long categoryId, Pageable pageable) {
		Page<ProductReponseDTO> productPage = this.productService.getProductByCategory(categoryId, pageable);
		return ApiResponse.success(PageResponse.from(productPage), "danh sách sảm phẩm theo danh mục");
	}

	@GetMapping("/productStock")
	public ResponseEntity<?> getProductStock(@RequestParam(defaultValue = "10") int threshold, Pageable pageable) {
		Page<ProductReponseDTO> productPage = this.productService.getProductStock(threshold, pageable);
		return ApiResponse.success(PageResponse.from(productPage), "Danh sách sản phẩm gần hết hàng");
	}

}
