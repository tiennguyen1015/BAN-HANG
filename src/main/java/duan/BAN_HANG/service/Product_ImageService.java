package duan.BAN_HANG.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import duan.BAN_HANG.exception.ResourceNotFoundException;
import duan.BAN_HANG.model.ProductImage;
import duan.BAN_HANG.model.Products;
import duan.BAN_HANG.reponseDTO.ProductImgReponseDTO;
import duan.BAN_HANG.reponseDTO.ProductReponseDTO;
import duan.BAN_HANG.repository.ProductRepository;
import duan.BAN_HANG.repository.Product_ImageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Product_ImageService {

	private final Product_ImageRepository product_ImageRepository;
	private final ProductRepository productRepository;

	private static final String UPLOAD_DIR = "/app/uploads";

	public ProductImgReponseDTO convertToDTO(ProductImage productImage) {
		ProductImgReponseDTO dto = ProductImgReponseDTO.builder().id(productImage.getId())
				.imageUrl(productImage.getImageUrl()).product(new ProductReponseDTO().builder()
						.id(productImage.getProduct().getId()).name(productImage.getProduct().getName()).build())
				.build();
		return dto;
	}

	public List<ProductImgReponseDTO> convertToDTOList(List<ProductImage> productImages) {

		return productImages.stream().map(this::convertToDTO).toList();
	}

	public List<ProductImgReponseDTO> getAllImage(Long productId) {
		Products products = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("sản phẩm không tồn tại"));
		List<ProductImage> productImages = this.product_ImageRepository.findByProductId(productId);

		return convertToDTOList(productImages);
	}

	// up 1 ảnh
//	public ProductImgReponseDTO uploadImages(MultipartFile file, Long productId) throws IOException {
//
//		Products products = this.productRepository.findById(productId)
//				.orElseThrow(() -> new ResourceNotFoundException("danh mục không tồn tại"));
//
//		File folder = new File(UPLOAD_DIR);
//		if (!folder.exists()) {
//			folder.mkdirs();
//		}
//
//		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
//		Path path = Paths.get(UPLOAD_DIR, fileName);
//		Files.copy(file.getInputStream(), path);
//
//		ProductImage image = new ProductImage();
//		image.setImageUrl(fileName);
//		image.setProduct(products);
//
//		ProductImage newProductImage = this.product_ImageRepository.save(image);
//		return convertToDTO(newProductImage);
//	}

	// up nhiều ảnh
	public List<ProductImgReponseDTO> uploadImages(List<MultipartFile> files, Long productId) throws IOException {

		Products products = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("danh mục không tồn tại"));
		File folder = new File(UPLOAD_DIR);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		List<ProductImgReponseDTO> result = new ArrayList<>();
		for (MultipartFile file : files) {
			// Đổi tên file để tránh trùng
			String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
			// Đường dẫn lưu file
			Path path = Paths.get(UPLOAD_DIR, fileName);
			// Lưu file xuống thư mục uploads
			Files.copy(file.getInputStream(), path);
			// Tạo bản ghi ProductImage
			ProductImage productImage = new ProductImage();
			productImage.setImageUrl(fileName);
			productImage.setProduct(products);
			// Lưu database
			ProductImage saved = product_ImageRepository.save(productImage);
			// Thêm vào danh sách kết quả
			result.add(convertToDTO(saved));
		}
		return result;
	}

	public ProductImgReponseDTO updateImage(MultipartFile files, Long ImageId) throws IOException {
		ProductImage productImage = this.product_ImageRepository.findById(ImageId)
				.orElseThrow(() -> new ResourceNotFoundException("không tìm thấy ảnh"));

		Path path = Paths.get(UPLOAD_DIR, productImage.getImageUrl());
		Files.deleteIfExists(path);

		String fileNew = UUID.randomUUID() + "_" + files.getOriginalFilename();
		Path newPath = Paths.get(UPLOAD_DIR, fileNew);
		productImage.setImageUrl(fileNew);
		ProductImage save = this.product_ImageRepository.save(productImage);
		return convertToDTO(save);
	}

	public void delete(Long ImageId) throws IOException {
		ProductImage productImage = this.product_ImageRepository.findById(ImageId)
				.orElseThrow(() -> new ResourceNotFoundException("không tìm thấy ảnh"));
		Path path = Paths.get(UPLOAD_DIR, productImage.getImageUrl());
		Files.deleteIfExists(path);
		this.product_ImageRepository.delete(productImage);
	}

}
