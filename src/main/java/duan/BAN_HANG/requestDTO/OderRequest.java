package duan.BAN_HANG.requestDTO;

import java.util.List;

import lombok.Data;

@Data
public class OderRequest {
	private List<OrderItemRequest> items;
	private String receiverName;
	private String receiverPhone;
	private String receiverEmail;
	private String receiverAddress;
	private String note;
	private String paymentMethod;
}
