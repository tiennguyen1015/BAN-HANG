package duan.BAN_HANG.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import duan.BAN_HANG.model.Order;
import duan.BAN_HANG.model.OrderStatus;
import duan.BAN_HANG.model.User;

@Repository
public interface OderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
	List<Order> findByUser(User user);

	Optional<Order> findByUserAndStatus(User user, OrderStatus status);

	List<Order> findByStatusAndUser(OrderStatus status, User user);

	Optional<Order> findByIdAndUser_Email(Long orderId, String email);

	@Query("""
			    SELECT COALESCE(SUM(o.totalPrice), 0)
			    FROM Order o
			    WHERE o.status = :status
			""")
	BigDecimal getTotalRevenueByStatus(@Param("status") OrderStatus status);

	long countByOrderCodeStartingWith(String prefix);

	// dang sách đơn hàng hôm nay
	@Query("""
			    SELECT o FROM Order o
			    WHERE o.createdAt >= :start
			    AND o.createdAt < :end
			    ORDER BY o.createdAt DESC
			""")
	Page<Order> findOrdersToday(@Param("start") Instant start, @Param("end") Instant end, Pageable pageable);

	// Số đơen hàng trong ngày hôm nay
	@Query("""
			    SELECT COUNT(o)
			    FROM Order o
			    WHERE o.createdAt >= :start
			    AND o.createdAt < :end
			""")
	Long countOrdersToday(Instant start, Instant end);

	// số đơn hàng đã giao thành công trong ngày hôm nay
	@Query("""
			 SELECT COUNT(o)
			 FROM Order o
			 WHERE o.status = 'DELIVERED'
			 AND o.deliveredAt >= :start
			 AND o.deliveredAt < :end
			""")
	Long countOrdersTodaysuccess(OrderStatus status, Instant start, Instant end);

	// Tổng doanh thu trong ngày
	@Query("""
			  SELECT COALESCE(SUM(o.totalPrice), 0)
			    FROM Order o
			    WHERE o.status = :status
			    AND o.deliveredAt >= :start
			    AND o.deliveredAt < :end
			""")
	BigDecimal getRevenueToday(OrderStatus status, Instant start, Instant end);

}
