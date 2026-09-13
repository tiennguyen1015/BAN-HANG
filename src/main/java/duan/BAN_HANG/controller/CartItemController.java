package duan.BAN_HANG.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import duan.BAN_HANG.reponseDTO.CartItemResponseDTO;
import duan.BAN_HANG.requestDTO.UpdateCartItemRequest;
import duan.BAN_HANG.service.CartItemService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CartItemController {
	private final CartItemService cartItemService;

	@PutMapping("/cart/items/{cartItemId}")
	public ResponseEntity<?> updateQuantity(@PathVariable Long cartItemId, @RequestBody UpdateCartItemRequest request) {
		CartItemResponseDTO response = this.cartItemService.updateQuantity(cartItemId, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/cart/items/{cartItemId}")
	public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId) {
		this.cartItemService.deleteCartItem(cartItemId);
		return ResponseEntity.ok("Xóa sản phẩm khỏi giỏ hàng thành công");
	}

}
