package duan.BAN_HANG.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import duan.BAN_HANG.service.OderItemService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OderItemController {
	private final OderItemService orderItemService;

	@PostMapping("/order/items/{orderItemId}")
	public String deleteOrderItem(@PathVariable("orderItemId") Long orderItemId) {

		this.orderItemService.deleteOrderItem(orderItemId);
		return "Xóa sản phẩm khỏi đơn hàng thành công";
	}

}
