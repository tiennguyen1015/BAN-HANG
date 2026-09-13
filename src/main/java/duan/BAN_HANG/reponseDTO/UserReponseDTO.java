package duan.BAN_HANG.reponseDTO;

import java.time.Instant;
import java.util.List;

import duan.BAN_HANG.model.Order;
import duan.BAN_HANG.model.Products;
import duan.BAN_HANG.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReponseDTO {
	private Long id;
	private String name;
	private String email;
//	private String password;
	private String address;
	private String phone;

	private String imageUrl;

	private Cart cart;
	private Role role;
	private List<Order> orders;
	private List<Review> reviews;
	private List<Products> products;

	@Setter
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Role {
		private Long id;
		private String name;
		private String description;
	}

	@Setter
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Cart {
		private Long id;
		private Instant createdAt;
	}

}
