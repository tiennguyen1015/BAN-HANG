package duan.BAN_HANG.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import duan.BAN_HANG.helper.ApiResponse;
import duan.BAN_HANG.model.User;
import duan.BAN_HANG.reponseDTO.CartItemResponseDTO;
import duan.BAN_HANG.reponseDTO.CartResponseDTO;
import duan.BAN_HANG.requestDTO.AddToCartRequest;
import duan.BAN_HANG.service.CartService;
import duan.BAN_HANG.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;

	private final UserService userService;

//	@PostMapping("/users/{userId}/cart")
//	public ResponseEntity<?> creatCart(@PathVariable Long userId) {
//		CartResponseDTO cartResponseDTO = this.cartService.createCart(userId);
//		return ApiResponse.success(cartResponseDTO, "thêm giỏ hàng thành công");
//	}

	@PostMapping("/cart")
	public ResponseEntity<?> creatCart(Authentication authentication, @AuthenticationPrincipal Jwt jwt) {
		String email = authentication.getName();
		User user = userService.findByEmail(email);
		Long userId = user.getId();
		Long id = jwt.getClaim("id");

		CartResponseDTO cartResponseDTO = this.cartService.createCart(userId);
		return ApiResponse.success(cartResponseDTO, "thêm giỏ hàng thành công");
	}

//	@GetMapping("/users/{userId}/cart")
//	public ResponseEntity<?> getCartUserId(@PathVariable Long userId) {
//		CartResponseDTO cartResponseDTO = this.cartService.getCart(userId);
//		return ApiResponse.success(cartResponseDTO, "danh sách giot hàng");
//	}

	@GetMapping("/cart")
	public ResponseEntity<?> getCartUserId(@AuthenticationPrincipal Jwt jwt) {
		Long userId = jwt.getClaim("id");
		CartResponseDTO cartResponseDTO = this.cartService.getCart(userId);
		return ApiResponse.success(cartResponseDTO, "danh sách giỏ hàng");
	}

	@PostMapping("/addCart")
	public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request) {
		CartItemResponseDTO cartItemResponseDTO = this.cartService.addToCart(request);
		return ApiResponse.success(cartItemResponseDTO, "thêm sản phẩm vào giỏ hàng thành công ");
	}

	@DeleteMapping("/cart/{cartItemId}")
	public ResponseEntity<?> deleteCart(@AuthenticationPrincipal Jwt jwt, @PathVariable Long cartItemId) {
		Long userId = jwt.getClaim("id");
		this.cartService.deleteCart(userId, cartItemId);
		return ApiResponse.success(null, "xóa giỏ hàng thành công");
	}

}
