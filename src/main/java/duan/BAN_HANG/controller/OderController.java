package duan.BAN_HANG.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import duan.BAN_HANG.helper.ApiResponse;
import duan.BAN_HANG.model.OrderStatus;
import duan.BAN_HANG.reponseDTO.OderItemResponseDTO;
import duan.BAN_HANG.reponseDTO.OderResponseDTO;
import duan.BAN_HANG.reponseDTO.PageResponse;
import duan.BAN_HANG.requestDTO.OderRequest;
import duan.BAN_HANG.service.OderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class OderController {

	private final OderService oderService;

	@GetMapping("/allorder")
	public ResponseEntity<?> getAllOder(String searchKeyword, Pageable pageable) {
		Page<OderResponseDTO> oderItemResponse = this.oderService.getAllOder(searchKeyword, pageable);
		return ApiResponse.success(oderItemResponse, "lấy giỏ hàng thành công");
	}

	@GetMapping("/order")
	public ResponseEntity<?> getAllOder(Authentication authentication) {
		List<OderResponseDTO> oderItemResponse = this.oderService.getOder(authentication);
		return ApiResponse.success(oderItemResponse, "lấy giỏ hàng thành công của user " + authentication.getName());
	}

	@GetMapping("/order/user/{orderId}")
	public ResponseEntity<?> getAllOder(Authentication authentication, @PathVariable("orderId") Long orderId) {
		OderResponseDTO oderItemResponse = this.oderService.getOderByOrderIdAndUser(authentication, orderId);
		return ApiResponse.success(oderItemResponse, "lấy chi tiết đơn hàng theo id thoianhf công ");
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<?> getAllOder(@PathVariable("orderId") Long orderId) {
		OderResponseDTO oderItemResponse = this.oderService.getOderId(orderId);
		return ApiResponse.success(oderItemResponse, "lấy giỏ hàng thành công theo id " + orderId);
	}

//	@GetMapping("/orderAdmin")
//	public ResponseEntity<?> getAllOder() {
//		List<OderResponseDTO> oderItemResponse = this.oderService.getOder();
//		return ApiResponse.success(oderItemResponse, "lấy giỏ hàng thành công của user " + authentication.getName());
//	}

	@GetMapping("/orderByUser")
	public ResponseEntity<?> getOderByUser(Authentication authentication) {
		List<OderResponseDTO> oderResponse = this.oderService.getOderByUser();
		return ApiResponse.success(oderResponse, "lấy đơn hàng của user thành công");
	}

	@PostMapping("/order")
	public ResponseEntity<?> order(@RequestBody OderRequest oderRequest) {
		List<OderItemResponseDTO> oderItemResponse = this.oderService.addToOder(oderRequest);
		return ApiResponse.success(oderItemResponse, "đặt hàng thành công");
	}

	@GetMapping("/orders/status/{status}")
	public ResponseEntity<?> getOrdersByStatus(@PathVariable("status") OrderStatus status) {
		List<OderResponseDTO> orderList = this.oderService.OrderByStatus(status);
		return ApiResponse.success(orderList, "Lấy danh sách đơn hàng theo trạng thái thành công");
	}

	@PutMapping("/orders/{orderId}/status/{status}")
	public ResponseEntity<?> updateOrderStatus(@PathVariable("orderId") Long orderId,
			@PathVariable("status") OrderStatus status) {
		OderResponseDTO oderResponseDTO = this.oderService.updateOrderStatus(orderId, status);
		return ApiResponse.success(oderResponseDTO, "Cập nhật trạng thái đơn hàng thành công");
	}

	@PutMapping("/orders/{orderId}/cancel")
	public ResponseEntity<?> cancelOrder(@PathVariable("orderId") Long orderId, Authentication authentication) {
		this.oderService.cancelOrder(orderId, authentication);
		return ApiResponse.success("Hủy đơn hàng thành công");
	}

	@PostMapping("/orders/{orderId}/buy-again")
	public ResponseEntity<?> buyAgain(@PathVariable("orderId") Long orderId, Authentication authentication) {
		this.oderService.buyAgain(orderId, authentication);
		return ResponseEntity.ok(Map.of("message", "Mua lại đơn hàng thành công"));
	}

	@GetMapping("/orders/today")
	public ResponseEntity<?> getOrderedToday(Pageable pageable) {
		Page<OderResponseDTO> products = this.oderService.getOrderedToday(pageable);
		return ApiResponse.success(PageResponse.from(products),
				"Lấy danh sách sản phẩm đã được đặt trong ngày thành công");
	}

}
