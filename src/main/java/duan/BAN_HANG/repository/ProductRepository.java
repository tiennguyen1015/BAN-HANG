package duan.BAN_HANG.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import duan.BAN_HANG.model.Categories;
import duan.BAN_HANG.model.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long>, JpaSpecificationExecutor<Products> {

	boolean existsByName(String name);

	boolean existsByCategory(Categories category);

	Page<Products> findByCategoryId(Long categoryId, Pageable pageable);

	@Query("""
			SELECT p FROM Products p WHERE p.stock < :threshold
			""")
	Page<Products> findProductsWithLowStock(@Param("threshold") int threshold, Pageable pageable);
}
