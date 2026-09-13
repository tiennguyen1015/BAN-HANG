package duan.BAN_HANG.config;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import duan.BAN_HANG.exception.ResourceNotFoundException;
import duan.BAN_HANG.model.RefreshToken;
import duan.BAN_HANG.model.User;
import duan.BAN_HANG.reponseDTO.ExchangeTokenResponse;
import duan.BAN_HANG.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

	public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;
	private final JwtEncoder jwtEncoder;
	private final RefreshTokenService refreshTokenService;

	private final RefreshTokenRepository RefreshTokenRepository;

	@Value("${banhang.jwt.validity-in-accToken-seconds}")
	private Long accessTokenExpiration;

	@Value("${banhang.jwt.validity-in-refresToken-seconds}")
	private Long refreshTokenExpiration;

	public String getScop(Authentication authentication) {
		// if trên trả về [ROLE_ADMIN, FACTOR_PASSWORD] ROLE_ADMIN → quyền admin của
		// người dùng.
//		FACTOR_PASSWORD → authority được Spring Security 7 thêm vào để biểu thị người dùng đã xác thực bằng mật khẩu.
		if (authentication != null) {
			String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.joining(" "));
			return scope;
		}

//		if (authentication != null) {
//			String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
//					.filter(a -> a.startsWith("ROLE_")).findFirst().orElse(null);
//			return scope;
//		}
		return "unknow";
	}

	public String creatAccessToken(Authentication authentication, Long userId) {
		Instant now = Instant.now();
		Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

		String scope = this.getScop(authentication);

		JwtClaimsSet claims = JwtClaimsSet.builder().issuedAt(now).expiresAt(validity).subject(authentication.getName())
				.claim("id", userId).claim("scope", scope).build();
		JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
		return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
	}

	public String generateSercToken() {
		byte[] randomBytes = new byte[64];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(randomBytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
	}

	public String createRefreshToken(User user) {
		Instant now = Instant.now();
		Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

		String Token = this.generateSercToken();
		RefreshToken rf = this.RefreshTokenRepository.findByUser(user).orElseGet(() -> {
			RefreshToken newToken = new RefreshToken();
			newToken.setUser(user);
			return newToken;
		});
		rf.setCreateAt(now);
		rf.setExpiresAt(validity);
		rf.setToken(Token);

		this.refreshTokenService.createRefreshToken(rf);
		return Token;

	}

	public ExchangeTokenResponse handleExchangeTokenResponse(String inputToken) {
		RefreshToken curentRefreshToken = this.refreshTokenService.findByToken(inputToken);

		Instant now = Instant.now();
		if (now.isAfter(curentRefreshToken.getExpiresAt())) {
			throw new ResourceNotFoundException("RefreshToken da het han");

		}
		User currenUser = curentRefreshToken.getUser();

		String newRefreshToken = this.createRefreshToken(currenUser);
		Instant validity = now.plus(Long.valueOf(accessTokenExpiration), ChronoUnit.SECONDS);
		String scope = "ROLE_" + currenUser.getRole().getName();
		JwtClaimsSet claims = JwtClaimsSet.builder().issuedAt(now).expiresAt(validity).subject(currenUser.getEmail())
				.claim("id", currenUser.getId()).claim("scope", scope).build();
		JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
		String accessToken = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

		ExchangeTokenResponse exToken = new ExchangeTokenResponse();
		exToken.setAccessToken(accessToken);
		exToken.setRefreshToken(newRefreshToken);
//		exToken.setUser(new LoginReponseDTO.UserLogin(currenUser.getId(), currenUser.getEmail(), this.getScop(null)));
//		this.refreshTokenService.deleteById(curentRefreshToken.getId());
//		this.refreshTokenService.createRefreshToken(exToken.getRefreshToken());
		return exToken;
	}

}
