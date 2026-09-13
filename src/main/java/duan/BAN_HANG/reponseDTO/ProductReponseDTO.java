package duan.BAN_HANG.reponseDTO;

import java.math.BigDecimal;
import java.util.List;

import duan.BAN_HANG.model.CartItem;
import duan.BAN_HANG.model.OrderItem;
import duan.BAN_HANG.model.Review;
import duan.BAN_HANG.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReponseDTO {
	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private Integer stock;
	private Categories category;

	private List<ProductImageDTO> images;
	private List<Review> reviews;
	private List<CartItem> cartItems;
	private List<OrderItem> orderItems;
	private List<User> users;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Categories {
		private Long id;
		private String name;
		private String description;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ProductImageDTO {
		private Long id;
		private String imageUrl;
	}

}
