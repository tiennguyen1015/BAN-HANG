package duan.BAN_HANG.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duan.BAN_HANG.model.Order;
import duan.BAN_HANG.model.OrderItem;
import duan.BAN_HANG.reponseDTO.OderItemResponseDTO;
import duan.BAN_HANG.reponseDTO.ProductReponseDTO;
import duan.BAN_HANG.reponseDTO.ProductReponseDTO.ProductImageDTO;
import duan.BAN_HANG.repository.OderItemRepository;
import duan.BAN_HANG.repository.OderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OderItemService {
	private final OderItemRepository orderItemRepository;
	private final OderRepository orderRepository;

	public OderItemResponseDTO convertToDTO(OrderItem oderItem) {
		return OderItemResponseDTO.builder().id(oderItem.getId()).quantity(oderItem.getQuantity())
				.price(oderItem.getPrice())
				.product(ProductReponseDTO.builder().id(oderItem.getProduct().getId())
						.name(oderItem.getProduct().getName()).description(oderItem.getProduct().getDescription())
						.price(oderItem.getProduct().getPrice()).stock(oderItem.getProduct().getStock())
						.images(oderItem.getProduct().getImages().stream().map(
								img -> ProductImageDTO.builder().id(img.getId()).imageUrl(img.getImageUrl()).build())
								.toList())
						.build())
				.build();
	}

	public OrderItem convertToEntity(OderItemResponseDTO oderItemDTO) {
		OrderItem orderItem = new OrderItem();
		orderItem.setId(oderItemDTO.getId());
		orderItem.setQuantity(oderItemDTO.getQuantity());
		orderItem.setPrice(oderItemDTO.getPrice());
		return orderItem;
	}

	@Transactional
	public void deleteOrderItem(Long orderItem) {
		System.out.println("Deleting OrderItem with ID: " + orderItem);
		OrderItem existingOrderItem = orderItemRepository.findById(orderItem)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy OrderItem"));

		Order order = existingOrderItem.getOrder();

		if (order.getStatus() != order.getStatus().PENDING) {
			throw new RuntimeException("Không thể xóa sản phẩm khỏi đơn hàng đã được xác nhận hoặc đã hoàn thành");
		}

//		orderItemRepository.delete(existingOrderItem);

		BigDecimal totalPrice = order.getTotalPrice().subtract(existingOrderItem.getPrice());

		order.setTotalPrice(totalPrice);
		System.out.println("Total Price after deletion: " + totalPrice);
//		orderRepository.save(order);
//		this.orderItemRepository.delete(existingOrderItem);
	}

}
