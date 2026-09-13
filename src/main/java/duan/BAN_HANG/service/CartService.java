package duan.BAN_HANG.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duan.BAN_HANG.exception.ResourceNotFoundException;
import duan.BAN_HANG.model.Cart;
import duan.BAN_HANG.model.CartItem;
import duan.BAN_HANG.model.Products;
import duan.BAN_HANG.model.User;
import duan.BAN_HANG.reponseDTO.CartItemResponseDTO;
import duan.BAN_HANG.reponseDTO.CartResponseDTO;
import duan.BAN_HANG.repository.CartItemRepository;
import duan.BAN_HANG.repository.CartRepository;
import duan.BAN_HANG.repository.ProductRepository;
import duan.BAN_HANG.repository.UserRepository;
import duan.BAN_HANG.requestDTO.AddToCartRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final UserService userService;
	private final ProductService productService;
	private final ProductRepository productRepository;
	private final CartItemRepository cartItemRepository;

	public CartResponseDTO convertToDTO(Cart cart) {

		return CartResponseDTO.builder().id(cart.getId()).createdAt(cart.getCreatedAt())
				.user(new CartResponseDTO().getUser())
				.cartItems(cart.getCartItems().stream().map(this::convertCartItemToDTO).toList()).build();
	}

	public CartItemResponseDTO convertCartItemToDTO(CartItem cartItem) {
		return CartItemResponseDTO.builder().id(cartItem.getId()).quantity(cartItem.getQuantity())
				.product(this.productService.convertToDTO(cartItem.getProduct())).build();

	}

	public List<CartItemResponseDTO> convertToListDTO(List<CartItem> cartItems) {
		return cartItems.stream().map(this::convertCartItemToDTO).toList();
	}

	public CartResponseDTO createCart(Long userId) {
		if (cartRepository.existsByUserId(userId)) {
			throw new ResourceNotFoundException("người dùng đã có giỏ hàng");
		}
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("không tìm thấy người dùng"));

		Cart cart = new Cart();
		cart.setCreatedAt(Instant.now());
		cart.setUser(user);

		Cart newCart = this.cartRepository.save(cart);

		return convertToDTO(newCart);
	}

	public CartResponseDTO getCart(Long userId) {

		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("không tìm thấy người dùng"));

		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));

		List<CartItem> cartItems = cart.getCartItems();

		BigDecimal totalPrice = BigDecimal.ZERO;

		for (CartItem item : cartItems) {

			BigDecimal itemTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

			totalPrice = totalPrice.add(itemTotal);
		}
		return CartResponseDTO.builder().id(cart.getId()).createdAt(cart.getCreatedAt()).totalPrice(totalPrice)
				.user(new CartResponseDTO.UserReponse(cart.getUser().getId(), cart.getUser().getName(),
						cart.getUser().getEmail(), cart.getUser().getAddress(), cart.getUser().getPhone()))
				.cartItems(convertToListDTO(cartItems)).build();
	}

	public CartItemResponseDTO addToCart(AddToCartRequest resquest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		User user = this.userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("user hông tồn tại"));

		Products products = this.productRepository.findById(resquest.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("sản phẩm không tồn taih"));

		if (resquest.getQuantity() > products.getStock()) {
			throw new ResourceNotFoundException("không đủ số lượng sản phẩm");
		}

		Cart cart = this.cartRepository.findByUser(user).orElse(null);

		if (cart == null) {
			cart = new Cart();
			cart.setUser(user);
			cart = this.cartRepository.save(cart);
		}

		CartItem cartItem = this.cartItemRepository.findByCartAndProduct(cart, products).orElse(null);

		if (cartItem == null) {
			cartItem = new CartItem();
			cartItem.setCart(cart);
			cartItem.setProduct(products);
			cartItem.setQuantity(resquest.getQuantity());
		} else {
			int newQuantity = cartItem.getQuantity() + resquest.getQuantity();
			if (newQuantity > products.getStock()) {
				throw new ResourceNotFoundException("số lượng vượt quá tồm kho");
			}
			cartItem.setQuantity(newQuantity);
		}

		CartItem newCartItem = this.cartItemRepository.save(cartItem);
		return convertCartItemToDTO(newCartItem);
	}

	public void deleteCart(Long userId, Long cartItemId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("không tìm thấy người dùng"));
		Cart cart = this.cartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("không tìm thấy giỏ hàng"));

		CartItem cartItem = this.cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException("không tìm thấy sản phẩm trong giỏ hàng"));

		this.cartItemRepository.delete(cartItem);
	}
}
