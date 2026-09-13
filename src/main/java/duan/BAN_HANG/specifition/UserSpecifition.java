package duan.BAN_HANG.specifition;

import org.springframework.data.jpa.domain.Specification;

import duan.BAN_HANG.model.User;

public class UserSpecifition {
	public static Specification<User> hasName(String name) {
		return (root, query, criteriaBuilder) -> {
			if (name == null || name.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.like(root.get("name"), "%" + name + "%");
		};
	}

	public static Specification<User> hasEmail(String email) {
		return (root, query, criteriaBuilder) -> {
			if (email == null || email.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.like(root.get("email"), "%" + email + "%");
		};
	}

	public static Specification<User> hasAddress(String address) {
		return (root, query, criteriaBuilder) -> {
			if (address == null || address.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.like(root.get("address"), "%" + address + "%");
		};
	}

	public static Specification<User> hasPhone(String phone) {
		return (root, query, criteriaBuilder) -> {
			if (phone == null || phone.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.like(root.get("phone"), "%" + phone + "%");
		};
	}

	public static Specification<User> hasRole(String role) {
		return (root, query, criteriaBuilder) -> {
			if (role == null || role.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.like(root.get("role").get("name"), "%" + role + "%");
		};
	}

	// tìm kiếm theo từ khóa
	public static Specification<User> hasSearchKeyword(String keyword) {
		return (root, query, cb) -> {
			if (keyword == null || keyword.trim().isEmpty()) {
				return null;
			}
			String like = "%" + keyword.toLowerCase() + "%";
			return cb.or(cb.like(cb.lower(root.get("name")), like), cb.like(cb.lower(root.get("email")), like),
					cb.like(cb.lower(root.get("role").get("name")), like),
					cb.like(root.get("phone"), "%" + keyword + "%"));
		};
	}

}
