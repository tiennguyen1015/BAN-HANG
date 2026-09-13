package duan.BAN_HANG.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import duan.BAN_HANG.exception.ResourceNotFoundException;
import duan.BAN_HANG.model.CartItem;
import duan.BAN_HANG.model.Order;
import duan.BAN_HANG.model.OrderItem;
import duan.BAN_HANG.model.OrderStatus;
import duan.BAN_HANG.model.Products;
import duan.BAN_HANG.model.User;
import duan.BAN_HANG.reponseDTO.OderItemResponseDTO;
import duan.BAN_HANG.reponseDTO.OderResponseDTO;
import duan.BAN_HANG.repository.CartItemRepository;
import duan.BAN_HANG.repository.OderItemRepository;
import duan.BAN_HANG.repository.OderRepository;
import duan.BAN_HANG.repository.ProductRepository;
import duan.BAN_HANG.repository.UserRepository;
import duan.BAN_HANG.requestDTO.OderRequest;
import duan.BAN_HANG.requestDTO.OrderItemRequest;
import duan.BAN_HANG.specifition.OrderSpecifition;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OderService {

	private final OderRepository orderRepository;
	private final OderItemRepository orderItemRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final OderItemService oderItemService;
	private final CartItemRepository cartItemRepository;

	public OderResponseDTO convertToDTO(Order oder) {
//		OderResponseDTO oderResponseDTO = new OderResponseDTO();
//		oderResponseDTO.setId(oder.getId());
//		oderResponseDTO.setOrderStatus(oder.getStatus());
//		oderResponseDTO.setTotalPrice(oder.getTotalPrice());
//		return oderResponseDTO;
		return OderResponseDTO.builder().id(oder.getId()).orderStatus(oder.getStatus()).totalPrice(oder.getTotalPrice())
				.receiverName(oder.getReceiverName()).receiverPhone(oder.getReceiverPhone())
				.receiverEmail(oder.getReceiverEmail()).receiverAddress(oder.getReceiverAddress()).note(oder.getNote())
				.paymentMethod(oder.getPaymentMethod()).orderCode(oder.getOrderCode()).createdAt(oder.getCreatedAt())
				.status(oder.getStatus())

				.orderItems(oder.getOrderItems().stream().map(this.oderItemService::convertToDTO).toList()).build();
	}

	public List<OderResponseDTO> convertListDTO(List<Order> orderList) {
		return orderList.stream().map(this::convertToDTO).toList();
	}

	public Page<OderResponseDTO> getAllOder(String searchKeyword, Pageable pageable) {
		Specification<Order> spec = Specification.allOf(OrderSpecifition.hasSearchKeyword(searchKeyword));
		return this.orderRepository.findAll(spec, pageable).map(this::convertToDTO);
	}

//	public List<OderItemResponseDTO> getOderByUser(Authentication authentication) {
//		authentication = SecurityContextHolder.getContext().getAuthentication();
//		String email = authentication.getName();
//
//		User user = userRepository.findByEmail(email)
//				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
//
//		List<Order> order = orderRepository.findByUser(user);
//
//	//		 List<OrderResponseDTO> response = new ArrayList<>();
//
//		List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
//		List<OderItemResponseDTO> response = new ArrayList<>();
//
//		for (OrderItem item : orderItems) {
//			response.add(this.oderItemService.convertToDTO(item));
//		}
//
//		return response;
//	}

	public List<OderResponseDTO> getOder(Authentication authentication) {
		authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
		List<Order> orders = orderRepository.findByUser(user);
		return this.convertListDTO(orders);
	}

	public OderResponseDTO getOderByOrderIdAndUser(Authentication authentication, Long orderId) {
		authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
		Order orders = orderRepository.findByIdAndUser_Email(orderId, email)
				.orElseThrow(() -> new ResourceNotFoundException("đơn hàng cảu bạn không tồn tại"));
		return this.convertToDTO(orders);
	}

	public OderResponseDTO getOderId(Long orderId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
		Order orders = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
		return this.convertToDTO(orders);
	}

	public List<OderResponseDTO> getOderByUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
		// Lấy tất cả Order của User
		List<Order> orders = orderRepository.findByUser(user);
		return this.convertListDTO(orders);
	}

	public OderResponseDTO updateOrderStatus(Long orderId, OrderStatus status) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
		if (!order.getUser().getId().equals(user.getId())) {
			throw new RuntimeException("Bạn không có quyền cập nhật đơn hàng này");
		}
		order.setStatus(status);
		order.setDeliveredAt(Instant.now());
		this.orderRepository.save(order);
		return this.convertToDTO(order);
	}

	private String generateOrderCode() {
		String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		long count = orderRepository.countByOrderCodeStartingWith("ORD-" + date);
		return String.format("ORD-%s-%05d", date, count + 1);
	}

	@Transactional
	public List<OderItemResponseDTO> addToOder(OderRequest oderRequest) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

		// Tìm đơn hàng PENDING
		Order order = orderRepository.findByUserAndStatus(user, OrderStatus.PENDING).orElse(null);

		if (order == null) {
			order = new Order();
			order.setUser(user);
			order.setStatus(OrderStatus.PENDING);
			order.setTotalPrice(BigDecimal.ZERO);
			order.setReceiverName(oderRequest.getReceiverName());
			order.setReceiverPhone(oderRequest.getReceiverPhone());
			order.setReceiverEmail(oderRequest.getReceiverEmail());
			order.setReceiverAddress(oderRequest.getReceiverAddress());
			order.setNote(oderRequest.getNote());
			order.setPaymentMethod(oderRequest.getPaymentMethod());
			order.setOrderCode(generateOrderCode());
			order = orderRepository.save(order);
		}

		List<OderItemResponseDTO> response = new ArrayList<>();

		for (OrderItemRequest itemRequest : oderRequest.getItems()) {

			// Lấy sản phẩm
			Products product = productRepository.findById(itemRequest.getProductId())
					.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

			// Kiểm tra tồn kho
			if (itemRequest.getQuantity() > product.getStock()) {
				throw new ResourceNotFoundException(product.getName() + " không đủ số lượng");
			}

			// Kiểm tra sản phẩm đã có trong Order chưa
			OrderItem orderItem = orderItemRepository.findByOrderAndProduct(order, product).orElse(null);

			BigDecimal itemPrice = product.getPrice();

			if (orderItem == null) {

				orderItem = new OrderItem();
				orderItem.setOrder(order);
				orderItem.setProduct(product);
				orderItem.setQuantity(itemRequest.getQuantity());
				orderItem.setPrice(itemPrice);

			} else {

				orderItem.setQuantity(orderItem.getQuantity() + itemRequest.getQuantity());

				itemPrice = product.getPrice();

				orderItem.setPrice(itemPrice);
			}

			// Lưu OrderItem
			OrderItem savedItem = orderItemRepository.save(orderItem);

			// Trừ tồn kho
			product.setStock(product.getStock() - itemRequest.getQuantity());
			productRepository.save(product);

			// Xóa CartItem
			CartItem cartItem = cartItemRepository.findByCartAndProduct(user.getCart(), product).orElse(null);

			if (cartItem != null) {
				cartItemRepository.delete(cartItem);
			}

			response.add(this.oderItemService.convertToDTO(savedItem));
		}
		// Tính lại tổng tiền
		BigDecimal totalPrice = orderItemRepository.findByOrder(order).stream()
				.map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		order.setTotalPrice(totalPrice);
		orderRepository.save(order);
		return response;
	}

	public List<OderResponseDTO> OrderByStatus(@PathVariable("status") OrderStatus status) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

		List<Order> orders = this.orderRepository.findByStatusAndUser(status, user);
		return this.convertListDTO(orders);
	}

	@Transactional
	public void cancelOrder(Long orderId, Authentication authentication) {
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
		// Kiểm tra đơn hàng có thuộc user đang đăng nhập không
		if (!order.getUser().getId().equals(user.getId())) {
			throw new RuntimeException("Bạn không có quyền hủy đơn hàng này");
		}
		// Chỉ cho phép hủy khi PENDING
		if (order.getStatus() != OrderStatus.PENDING) {
			throw new RuntimeException("Chỉ có thể hủy đơn hàng khi đang chờ xác nhận");
		}
		// Hoàn lại stock
		for (OrderItem item : order.getOrderItems()) {
			Products product = item.getProduct();
			product.setStock(product.getStock() + item.getQuantity());
			productRepository.save(product);
		}
		// Đổi trạng thái đơn hàng
		order.setStatus(OrderStatus.CANCELLED);

		orderRepository.save(order);
	}

	@Transactional
	public void buyAgain(Long orderId, Authentication authentication) {

		String email = authentication.getName();

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));

		// Kiểm tra đơn hàng thuộc user hiện tại
		if (!order.getUser().getId().equals(user.getId())) {
			throw new RuntimeException("Bạn không có quyền mua lại đơn hàng này");
		}

		// Kiểm tra cart
		if (user.getCart() == null) {
			throw new RuntimeException("Không tìm thấy giỏ hàng");
		}

		for (OrderItem orderItem : order.getOrderItems()) {

			Products product = orderItem.getProduct();

			// Sản phẩm không còn tồn tại
			if (product == null) {
				continue;
			}

			// Sản phẩm hết hàng
			if (product.getStock() <= 0) {
				continue;
			}

			// Số lượng muốn mua lại
			int quantity = orderItem.getQuantity();

			// Nếu tồn kho ít hơn số lượng cũ
			if (quantity > product.getStock()) {
				quantity = product.getStock();
			}

			// Kiểm tra sản phẩm đã có trong cart chưa
			CartItem cartItem = cartItemRepository.findByCartAndProduct(user.getCart(), product).orElse(null);

			if (cartItem == null) {

				// Chưa có → tạo mới
				cartItem = new CartItem();

				cartItem.setCart(user.getCart());
				cartItem.setProduct(product);
				cartItem.setQuantity(quantity);

				cartItemRepository.save(cartItem);

			} else {

				// Đã có → cộng thêm số lượng
				int newQuantity = cartItem.getQuantity() + quantity;

				// Không vượt quá stock
				if (newQuantity > product.getStock()) {
					newQuantity = product.getStock();
				}

				cartItem.setQuantity(newQuantity);

				cartItemRepository.save(cartItem);
			}
		}
	}

	public Page<OderResponseDTO> getOrderedToday(Pageable pageable) {
		ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");
		LocalDate today = LocalDate.now(zone);
		Instant start = today.atStartOfDay(zone).toInstant();
		Instant end = today.plusDays(1).atStartOfDay(zone).toInstant();
		Page<Order> orders = this.orderRepository.findOrdersToday(start, end, pageable);
		return orders.map(this::convertToDTO);
	}

}
