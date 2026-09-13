package duan.BAN_HANG.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import duan.BAN_HANG.exception.ResourceNotFoundException;
import duan.BAN_HANG.model.Categories;
import duan.BAN_HANG.model.ProductImage;
import duan.BAN_HANG.model.Products;
import duan.BAN_HANG.reponseDTO.ProductReponseDTO;
import duan.BAN_HANG.reponseDTO.ProductReponseDTO.ProductImageDTO;
import duan.BAN_HANG.reponseFillterDTO.ProductFilterDTO;
import duan.BAN_HANG.repository.CategorieRepository;
import duan.BAN_HANG.repository.ProductRepository;
import duan.BAN_HANG.repository.Product_ImageRepository;
import duan.BAN_HANG.requestDTO.ProductRequestDTO;
import duan.BAN_HANG.specifition.ProductSpecifition;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final Product_ImageRepository product_ImageRepository;
	private final ProductRepository productRepository;
	private final CategorieRepository categorieRepository;
	private static final String UPLOAD_DIR = "uploads";

	public ProductReponseDTO convertToDTO(Products product) {
		return ProductReponseDTO.builder().id(product.getId()).name(product.getName())
				.description(product.getDescription()).price(product.getPrice()).stock(product.getStock())
				.category(new ProductReponseDTO.Categories(product.getCategory().getId(),
						product.getCategory().getName(), product.getCategory().getDescription()))
				.images(product.getImages().stream()
						.map(img -> ProductImageDTO.builder().id(img.getId()).imageUrl(img.getImageUrl()).build())
						.toList())

				.build();
	}

	public Page<ProductReponseDTO> converoPageDTO(Page<Products> productPage) {
		return productPage.map(this::convertToDTO);
	}

	public Products convertToProduct(ProductRequestDTO productDTO) {
		Products product = new Products();
		product.setId(productDTO.getId());
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		product.setStock(productDTO.getStock());
		return product;
	}

	public Page<ProductReponseDTO> getAllProducts(String searchKeyword, Pageable pageable) {
		Specification<Products> spec = Specification.anyOf(ProductSpecifition.hasSearchKeyword(searchKeyword));
		return this.productRepository.findAll(spec, pageable).map(this::convertToDTO);
	}

	public Page<ProductReponseDTO> getAllProducts1(ProductFilterDTO productFilterDTO, Pageable pageable) {
		Specification<Products> spec = Specification.allOf(ProductSpecifition.hasSearchKeyword1(productFilterDTO),
				ProductSpecifition.hasPriceBetween(productFilterDTO), ProductSpecifition.hasPrice(productFilterDTO)

		);
		return this.productRepository.findAll(spec, pageable).map(this::convertToDTO);
	}

	public ProductReponseDTO createProduct(ProductRequestDTO productDTO, List<MultipartFile> files) throws IOException {
		if (this.productRepository.existsByName(productDTO.getName())) {
			throw new ResourceNotFoundException("Tên sản phẩm đã tồn tại");
		}

		System.out.println(productDTO.getCategory().getId());

		if (productDTO.getCategory() == null || productDTO.getCategory().getId() == null) {
			throw new ResourceNotFoundException("Danh mục sản phẩm không được để trống");
		}

		Categories category = this.categorieRepository.findCategoryById(productDTO.getCategory().getId()).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy danh mục với id: " + productDTO.getId()));

		Products product = new Products();
		product.setName(productDTO.getName().trim());
		product.setDescription(productDTO.getDescription().trim());
		product.setPrice(productDTO.getPrice());
		product.setStock(productDTO.getStock());
		product.setCategory(category);
		this.productRepository.save(product);

		if (files != null && !files.isEmpty()) {

			File folder = new File(UPLOAD_DIR);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			for (MultipartFile file : files) {
				if (file.isEmpty()) {
					continue;
				}
				String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
				Path path = Paths.get(UPLOAD_DIR, fileName);
				Files.copy(file.getInputStream(), path);
				// Tạo bản ghi ProductImage
				ProductImage productImage = new ProductImage();
				productImage.setImageUrl(fileName);
				productImage.setProduct(product);
				// Lưu database
				ProductImage savedImage = product_ImageRepository.save(productImage);
				product.getImages().add(savedImage);
			}
		}

		Products savedProduct = productRepository.findById(product.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

		return convertToDTO(savedProduct);
	}

	public void updateProduct(ProductRequestDTO productDTO, List<MultipartFile> files, List<Long> deleteImageIds,
			Long id) throws IOException {
		Products products = this.productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id: " + id));

		if (productDTO.getCategory() == null || productDTO.getCategory().getId() == null) {
			throw new ResourceNotFoundException("Danh mục sản phẩm không được để trống");
		}

		Categories categories = this.categorieRepository.findCategoryById(productDTO.getCategory().getId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Không tìm thấy danh mục với id: " + productDTO.getCategory().getId()));
		products.setName(productDTO.getName().trim());
		products.setDescription(productDTO.getDescription().trim());
		products.setPrice(productDTO.getPrice());
		products.setStock(productDTO.getStock());
		products.setCategory(categories);
		this.productRepository.save(products);

		// Xóa ảnh được chọn
		if (deleteImageIds != null) {

			for (Long imageId : deleteImageIds) {

				ProductImage image = product_ImageRepository.findById(imageId)
						.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ảnh"));
				Files.deleteIfExists(Paths.get(UPLOAD_DIR, image.getImageUrl()));
				product_ImageRepository.delete(image);
			}
		}

		if (files != null && !files.isEmpty()) {

			File folder = new File(UPLOAD_DIR);

			if (!folder.exists()) {
				folder.mkdirs();
			}

			for (MultipartFile file : files) {

				if (file.isEmpty()) {
					continue;
				}
				String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

				Path path = Paths.get(UPLOAD_DIR, fileName);

				Files.copy(file.getInputStream(), path);

				ProductImage productImage = new ProductImage();
				productImage.setImageUrl(fileName);
				productImage.setProduct(products);

				product_ImageRepository.save(productImage);
			}
		}

	}

	public ProductReponseDTO getProductById(Long id) {
		Products products = this.productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id: " + id));
		return convertToDTO(products);
	}

	public void deleteProduct(Long id) {
		Products products = this.productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id: " + id));
		for (ProductImage Image : products.getImages()) {
			try {
				Path path = Paths.get(UPLOAD_DIR).resolve(Image.getImageUrl());
				Files.deleteIfExists(path);
			} catch (Exception e) {
				throw new ResourceNotFoundException("xóa file ảnh thất bại");
			}
		}

		this.productRepository.delete(products);
	}

	public Page<ProductReponseDTO> getProductByCategory(Long categoryId, Pageable pageable) {
		Page<Products> productPage = this.productRepository.findByCategoryId(categoryId, pageable);
		return converoPageDTO(productPage);
	}

	public Page<ProductReponseDTO> getProductStock(int threshold, Pageable pageable) {
		Page<Products> productPage = this.productRepository.findProductsWithLowStock(threshold, pageable);
		return converoPageDTO(productPage);
	}

}
