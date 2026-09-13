package duan.BAN_HANG.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import duan.BAN_HANG.model.Categories;

public interface CategorieRepository extends JpaRepository<Categories, Long>, JpaSpecificationExecutor<Categories> {

	boolean existsByName(String name);

	Optional<Categories> findCategoryById(Long id);
}
