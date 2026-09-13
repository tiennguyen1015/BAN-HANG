package duan.BAN_HANG.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import duan.BAN_HANG.model.OrderStatus;
import duan.BAN_HANG.reponseDTO.DashboardResponseDTO;
import duan.BAN_HANG.repository.OderItemRepository;
import duan.BAN_HANG.repository.OderRepository;
import duan.BAN_HANG.repository.ProductRepository;
import duan.BAN_HANG.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final OderRepository oderRepository;
	private final OderItemRepository oderItemRepository;

	public DashboardResponseDTO getDashboardData() {

		Long totalProducts = this.productRepository.count();
		Long totalUsers = this.userRepository.count();
		Long totalOrders = this.oderRepository.count();
		BigDecimal totalRevenue = oderRepository.getTotalRevenueByStatus(OrderStatus.DELIVERED);

		ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
		LocalDate today = LocalDate.now(zoneId);
		Instant todayStart = today.atStartOfDay(zoneId).toInstant();
		Instant todayEnd = today.plusDays(1).atStartOfDay(zoneId).toInstant();

		BigDecimal revenueToday = this.oderRepository.getRevenueToday(OrderStatus.DELIVERED, todayStart, todayEnd);
		Long ordersToday = this.oderRepository.countOrdersToday(todayStart, todayEnd);

		Long ordersTodaysuccessful = this.oderRepository.countOrdersTodaysuccess(OrderStatus.DELIVERED, todayStart,
				todayEnd);

		Long totalProductsToday = this.oderItemRepository.countProductsSoldToday(OrderStatus.DELIVERED, todayStart,
				todayEnd);
		if (totalProductsToday == null) {
			totalProductsToday = 0L;
		}

		return DashboardResponseDTO.builder().totalProducts(totalProducts).totalUsers(totalUsers)
				.totalOrders(totalOrders).totalRevenue(totalRevenue).totalProductsToday(totalProductsToday)
				.ordersToday(ordersToday).revenueToday(revenueToday).ordersTodaysuccessful(ordersTodaysuccessful)
				.build();
	}

	public Long getProductsSoldToday() {
		ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
		LocalDate today = LocalDate.now(zoneId);
		Instant todayStart = today.atStartOfDay(zoneId).toInstant();
		Instant todayEnd = today.plusDays(1).atStartOfDay(zoneId).toInstant();
		Long productsSoldToday = this.oderItemRepository.countProductsSoldToday(OrderStatus.DELIVERED, todayStart,
				todayEnd);

		return productsSoldToday;
	}

}
