package duan.BAN_HANG.reponseDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

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
public class CartResponseDTO {

	private Long id;
	private Instant createdAt;
	private BigDecimal totalPrice;
	private UserReponse user;
	private List<CartItemResponseDTO> cartItems;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class UserReponse {
		private Long id;
		private String name;
		private String email;
//		private String password;
		private String address;
		private String phone;
	}

}
