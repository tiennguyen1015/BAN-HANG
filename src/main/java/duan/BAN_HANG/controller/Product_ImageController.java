package duan.BAN_HANG.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import duan.BAN_HANG.helper.ApiResponse;
import duan.BAN_HANG.reponseDTO.ProductImgReponseDTO;
import duan.BAN_HANG.service.Product_ImageService;
import lombok.RequiredArgsConstructor;

@RestController
//@RequestMapping("/products/{productId}/images")
@RequiredArgsConstructor
public class Product_ImageController {

	private final Product_ImageService product_ImageService;

	@GetMapping("/products/{productId}/images")
	public ResponseEntity<?> getImageByProductId(@PathVariable Long productId) {
		List<ProductImgReponseDTO> productImages = this.product_ImageService.getAllImage(productId);
		return ApiResponse.success(productImages, "danh sách ảnh theo id sản phẩm :" + productId);
	}

	@PostMapping("/products/{productId}/images")
	public ResponseEntity<?> uploadImage(@RequestParam("file") List<MultipartFile> files, @PathVariable Long productId)
			throws IOException {
		List<ProductImgReponseDTO> productImage = this.product_ImageService.uploadImages(files, productId);
		return ApiResponse.success(productImage, "thêm ảnh thành công");

	}

	@PutMapping("/product-images/{ImageId}")
	public ResponseEntity<?> updateImage(@RequestParam("file") MultipartFile files, @PathVariable Long ImageId)
			throws IOException {
		ProductImgReponseDTO productImage = this.product_ImageService.updateImage(files, ImageId);
		return ApiResponse.success("cập nhật thành công");
	}

	@DeleteMapping("/product-images/{ImageId}")
	public ResponseEntity<?> deleteImage(@PathVariable Long ImageId) throws IOException {
		this.product_ImageService.delete(ImageId);
		return ApiResponse.success("xóa thành công");

	}

}
