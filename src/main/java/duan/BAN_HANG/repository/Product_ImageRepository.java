package duan.BAN_HANG.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import duan.BAN_HANG.model.ProductImage;

public interface Product_ImageRepository extends JpaRepository<ProductImage, Long> {
	List<ProductImage> findByProductId(Long id);
}
