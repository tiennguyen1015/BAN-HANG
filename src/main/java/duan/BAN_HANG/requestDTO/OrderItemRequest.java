package duan.BAN_HANG.requestDTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemRequest {
	private Long productId;
	private Integer quantity;
	private BigDecimal price;
}
