package duan.BAN_HANG.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import duan.BAN_HANG.model.Order;
import duan.BAN_HANG.model.OrderItem;
import duan.BAN_HANG.model.OrderStatus;
import duan.BAN_HANG.model.Products;

public interface OderItemRepository extends JpaRepository<OrderItem, Long> {

	Optional<OrderItem> findByOrderAndProduct(Order order, Products product);

	List<OrderItem> findByOrder(Order order);

//	@Query("""
//			SELECT SUM(oi.quantity) FROM OrderItem oi
//			WHERE oi.order.createdAt >= :startDate AND oi.order.createdAt < :endDate
//			""")
//	Long countProductsSoldToday(Instant startDate, Instant endDate);

	@Query("""
			SELECT SUM(oi.quantity) FROM OrderItem oi
			WHERE
			oi.order.status = 'DELIVERED' AND
			oi.order.deliveredAt  >= :startDate AND oi.order.deliveredAt  < :endDate
			""")
	Long countProductsSoldToday(OrderStatus status, Instant startDate, Instant endDate);
}
