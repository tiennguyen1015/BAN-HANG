package duan.BAN_HANG.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duan.BAN_HANG.exception.ResourceNotFoundException;
import duan.BAN_HANG.model.CartItem;
import duan.BAN_HANG.model.Products;
import duan.BAN_HANG.reponseDTO.CartItemResponseDTO;
import duan.BAN_HANG.repository.CartItemRepository;
import duan.BAN_HANG.requestDTO.UpdateCartItemRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService {
	private final CartItemRepository cartItemRepository;

	private final ProductService productService;

	public CartItemResponseDTO convertCartItemToDTO(CartItem cartItem) {
		return CartItemResponseDTO.builder().id(cartItem.getId()).quantity(cartItem.getQuantity())
				.product(this.productService.convertToDTO(cartItem.getProduct())).build();

	}

	public List<CartItemResponseDTO> convertToListDTO(List<CartItem> cartItems) {
		return cartItems.stream().map(this::convertCartItemToDTO).toList();
	}

	@Transactional
	public CartItemResponseDTO updateQuantity(Long cartItemId, UpdateCartItemRequest request) {

		CartItem cartItem = cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CartItem"));

		Products product = cartItem.getProduct();

		// kiểm tra tồn kho
		if (request.getQuantity() > product.getStock()) {
			throw new RuntimeException("Số lượng vượt quá tồn kho");
		}
		cartItem.setQuantity(request.getQuantity());
		cartItemRepository.save(cartItem);
		return convertCartItemToDTO(cartItem);
	}

	public void deleteCartItem(Long cartItemId) {
		CartItem cartItem = cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CartItem"));
		cartItemRepository.delete(cartItem);
	}

}
