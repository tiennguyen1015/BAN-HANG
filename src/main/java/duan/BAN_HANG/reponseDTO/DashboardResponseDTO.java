package duan.BAN_HANG.reponseDTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponseDTO {
	private Long totalProducts;
	private Long totalUsers;
	private Long totalOrders;
	private BigDecimal totalRevenue;

	private BigDecimal revenueToday;
	private Long ordersToday;
	private Long ordersTodaysuccessful;
	private Long totalProductsToday;

}
