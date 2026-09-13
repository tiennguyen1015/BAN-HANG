package duan.BAN_HANG.specifition;

import org.springframework.data.jpa.domain.Specification;

import duan.BAN_HANG.model.Order;

public class OrderSpecifition {
	public static Specification<Order> hasSearchKeyword(String searchKeyword) {
		return (root, query, criteriaBuilder) -> {
			if (searchKeyword == null || searchKeyword.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			String likePattern = "%" + searchKeyword.toLowerCase() + "%";
			return criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), likePattern),
					criteriaBuilder.like(criteriaBuilder.lower(root.get("totalPrice").as(String.class)), likePattern));
		};
	}

}
