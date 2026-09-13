package duan.BAN_HANG.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import duan.BAN_HANG.config.JwtService;
import duan.BAN_HANG.config.RefreshTokenService;
import duan.BAN_HANG.helper.ApiResponse;
import duan.BAN_HANG.model.RefreshToken;
import duan.BAN_HANG.model.User;
import duan.BAN_HANG.reponseDTO.ExchangeTokenResponse;
import duan.BAN_HANG.reponseDTO.LoginReponseDTO;
import duan.BAN_HANG.reponseDTO.UserReponseDTO;
import duan.BAN_HANG.requestDTO.LoginRequestDTO;
import duan.BAN_HANG.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {
	private final RefreshTokenService refreshTokenService;
	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final JwtService jwtService;

	@Value("${banhang.jwt.validity-in-accToken-seconds}")
	private String accToken;

	@Value("${banhang.jwt.validity-in-refresToken-seconds}")
	private Long refresToken;

	@PostMapping("login")
	public ResponseEntity<?> postLogin(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
		Authentication authentication = authenticationManager.authenticate(authToken);
		User user = this.userService.findByEmail(authentication.getName());

		String accessToken = this.jwtService.creatAccessToken(authentication, user.getId());
		String refreshToken = this.jwtService.createRefreshToken(user);

		LoginReponseDTO loginResponDto = new LoginReponseDTO();
		loginResponDto.setAccessToken(accessToken);
		loginResponDto.setRefreshToken(refreshToken);
		loginResponDto.setUser(new LoginReponseDTO.UserLogin(user.getId(), authentication.getName(),
				this.jwtService.getScop(authentication)));

		ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken).httpOnly(true).secure(false).path("/")
				.maxAge(refresToken).sameSite("None").build();
		ApiResponse<LoginReponseDTO> finalData = new ApiResponse<LoginReponseDTO>(HttpStatus.OK, "", loginResponDto,
				"");

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(finalData);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> postRefreshToken(@RequestParam("refreshToken") String Refresh) {
		ExchangeTokenResponse ref = this.jwtService.handleExchangeTokenResponse(Refresh);
		return ApiResponse.success(ref);
	}

	@PostMapping("refresh-with-cookei")
	public ResponseEntity<?> postWithCookei(@CookieValue(required = false) String refreshToken) {
		ExchangeTokenResponse ref = this.jwtService.handleExchangeTokenResponse(refreshToken);

		ResponseCookie coookeiRefresh = ResponseCookie.from("refreshToken", ref.getRefreshToken()).httpOnly(true)
				.secure(false).path("/").maxAge(refresToken).sameSite("None").build();
		ApiResponse<ExchangeTokenResponse> finalData = new ApiResponse<ExchangeTokenResponse>(HttpStatus.OK, "", ref,
				"");
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, coookeiRefresh.toString()).body(finalData);
	}

	@PostMapping("logout")
	public ResponseEntity<?> postLogout(// @AuthenticationPrincipal Jwt jwt, có thể dùng thay cho dùng
										// SecurityContextHolder
			@CookieValue(required = false) String refreshToken) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Jwt jwt = (Jwt) auth.getPrincipal();

		String userId = jwt.getClaimAsString("id");
		String username = jwt.getSubject();
		RefreshToken currentTokenInDB = this.refreshTokenService.findByToken(refreshToken);
		this.refreshTokenService.deleteById(currentTokenInDB.getId());
		ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", null).httpOnly(true).secure(true).path("/")
				.maxAge(0).build();

		ApiResponse<String> finalData = new ApiResponse<>(HttpStatus.OK, "", "ok", "");
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString()).body(finalData);
	}

	@PostMapping("register")
	public ResponseEntity<ApiResponse<UserReponseDTO>> postRegister(@Valid @RequestBody User user) {
		UserReponseDTO newUser = this.userService.Register(user);
		return ApiResponse.success(newUser, "đăng kí thành công");
	}

}
