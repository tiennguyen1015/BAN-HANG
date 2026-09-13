package duan.BAN_HANG.specifition;

import org.springframework.data.jpa.domain.Specification;

import duan.BAN_HANG.model.Categories;
import duan.BAN_HANG.model.Products;
import duan.BAN_HANG.reponseFillterDTO.ProductFilterDTO;
import jakarta.persistence.criteria.Join;

public class ProductSpecifition {
	public static Specification<Products> hasName(String name) {
		return (root, query, cb) -> {
			if (name == null || name.isEmpty()) {
				return cb.conjunction();
			}
			return cb.like(root.get("name"), "%" + name + "%");
		};
	}

	public static Specification<Products> hasDescription(String description) {
		return (root, query, cb) -> {
			if (description == null || description.isEmpty()) {
				return cb.conjunction();
			}
			return cb.like(root.get("description"), "%" + description + "%");
		};
	}

//	public static Specification<Products> hasPrice(BigDecimal price) {
//		return (root, query, cb) -> {
//			if (price == null) {
//				return cb.conjunction();
//			}
//			return cb.equal(root.get("price"), price);
//		};
//	}

//	public static Specification<Products> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
//		return (root, query, cb) -> {
//
//			if (minPrice == null && maxPrice == null) {
//				return cb.conjunction();
//			}
//
//			if (minPrice != null && maxPrice != null) {
//				return cb.between(root.get("price"), minPrice, maxPrice);
//			}
//
//			if (minPrice != null) {
//				return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
//			}
//
//			return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
//		};
//	}

	public static Specification<Products> hasStock(Integer stock) {
		return (root, query, cb) -> {
			if (stock == null) {
				return cb.conjunction();
			}
			return cb.equal(root.get("stock"), stock);
		};
	}

	public static Specification<Products> hasSearchKeyword(String searchKeyword) {
		return (root, query, cb) -> {
			if (searchKeyword == null || searchKeyword.isEmpty()) {
				return cb.conjunction();
			}

			Join<Products, Categories> category = root.join("category");

			String likePattern = "%" + searchKeyword + "%";
			return cb.or(cb.like(root.get("name"), likePattern), cb.like(root.get("description"), likePattern),
					cb.like(category.get("name"), likePattern)

			);
		};
	}

	public static Specification<Products> hasSearchKeyword1(ProductFilterDTO productFilterDTO) {
		return (root, query, cb) -> {
			if (productFilterDTO.getSearchKeyword() == null || productFilterDTO.getSearchKeyword().isEmpty()) {
				return cb.conjunction();
			}
			String likePattern = "%" + productFilterDTO.getSearchKeyword() + "%";
			return cb.or(cb.like(root.get("name"), likePattern), cb.like(root.get("description"), likePattern));
		};
	}

	public static Specification<Products> hasPriceBetween(ProductFilterDTO productFilterDTO) {
		return (root, query, cb) -> {

			if (productFilterDTO.getMinPrice() == null && productFilterDTO.getMaxPrice() == null) {
				return cb.conjunction();
			}

			if (productFilterDTO.getMinPrice() != null && productFilterDTO.getMaxPrice() != null) {
				return cb.between(root.get("price"), productFilterDTO.getMinPrice(), productFilterDTO.getMaxPrice());
			}

			if (productFilterDTO.getMinPrice() != null) {
				return cb.greaterThanOrEqualTo(root.get("price"), productFilterDTO.getMinPrice());
			}

			return cb.lessThanOrEqualTo(root.get("price"), productFilterDTO.getMaxPrice());
		};
	}

	public static Specification<Products> hasPrice(ProductFilterDTO productFilterDTO) {
		return (root, query, cb) -> {
			if (productFilterDTO.getPrice() == null) {
				return cb.conjunction();
			}
			return cb.equal(root.get("price"), productFilterDTO.getPrice());
		};
	}

}
