package duan.BAN_HANG.reponseDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import duan.BAN_HANG.model.OrderStatus;
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
public class OderResponseDTO {
	private Long id;
	private BigDecimal totalPrice;
	private OrderStatus orderStatus;
	private UserReponseDTO user;
	private Instant createdAt;
	private OrderStatus status;

	private String receiverName;
	private String receiverPhone;
	private String receiverEmail;
	private String receiverAddress;
	private String note;
	private String paymentMethod;
	private String orderCode;

	private List<OderItemResponseDTO> orderItems = new ArrayList();
}
