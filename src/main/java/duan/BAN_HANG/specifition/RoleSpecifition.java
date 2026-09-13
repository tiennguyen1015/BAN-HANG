package duan.BAN_HANG.specifition;

import org.springframework.data.jpa.domain.Specification;

import duan.BAN_HANG.model.Role;
import duan.BAN_HANG.reponseFillterDTO.RoleFilterDTO;

public class RoleSpecifition {
	public static Specification<Role> hasSearcKeyword(RoleFilterDTO roleFilterDTO) {

		return (root, query, cb) -> {
			if (roleFilterDTO.getSearchKeyword() == null || roleFilterDTO.getSearchKeyword().isEmpty()) {
				return cb.conjunction();
			}
			String keyWord = "%" + roleFilterDTO.getSearchKeyword() + "%";
			return cb.or(cb.like(root.get("name"), keyWord), cb.like(root.get("description"), keyWord));
		};

	}

}
