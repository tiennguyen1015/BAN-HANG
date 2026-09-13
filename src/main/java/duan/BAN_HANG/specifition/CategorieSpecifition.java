package duan.BAN_HANG.specifition;

import org.springframework.data.jpa.domain.Specification;

import duan.BAN_HANG.model.Categories;

public class CategorieSpecifition {
	public static Specification<Categories> hasSearchKeyword(String keyword) {
		return (root, query, cb) -> {
			if (keyword == null || keyword.trim().isEmpty()) {
				return null;
			}
			String like = "%" + keyword.toLowerCase() + "%";
			return cb.or(cb.like(cb.lower(root.get("name")), like), cb.like(cb.lower(root.get("description")), like));
		};
	}

	public static Specification<Categories> hasName(String name) {
		return (root, query, criteriaBuilder) -> {
			if (name == null || name.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.like(root.get("name"), "%" + name + "%");
		};
	}

	public static Specification<Categories> hasDescription(String description) {
		return (root, query, criteriaBuilder) -> {
			if (description == null || description.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.like(root.get("description"), "%" + description + "%");
		};
	}
}
