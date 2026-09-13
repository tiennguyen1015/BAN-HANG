package duan.BAN_HANG.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duan.BAN_HANG.helper.ApiResponse;
import duan.BAN_HANG.reponseDTO.DashboardResponseDTO;
import duan.BAN_HANG.service.DashboardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
	private final DashboardService dashboardService;

	@GetMapping()
	public ResponseEntity<?> getDashboardData() {
		DashboardResponseDTO dashboardData = this.dashboardService.getDashboardData();
		return ApiResponse.success(dashboardData, "Lấy dữ liệu dashboard thành công");
	}

	@GetMapping("/products-sold-today")
	public ResponseEntity<?> getProductsSoldToday() {
		Long productsSoldToday = this.dashboardService.getProductsSoldToday();
		return ApiResponse.success(productsSoldToday, "Lấy số lượng sản phẩm đã bán hôm nay thành công");
	}

}
