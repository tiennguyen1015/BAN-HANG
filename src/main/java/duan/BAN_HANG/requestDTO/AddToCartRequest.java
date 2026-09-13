package duan.BAN_HANG.requestDTO;

import lombok.Data;

@Data
public class AddToCartRequest {

	private Long productId;
	private Integer quantity;
}
