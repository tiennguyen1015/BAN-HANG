package duan.BAN_HANG.config;

import org.springframework.stereotype.Service;

import duan.BAN_HANG.exception.ResourceNotFoundException;
import duan.BAN_HANG.model.RefreshToken;
import duan.BAN_HANG.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	public void createRefreshToken(RefreshToken rf) {
		this.refreshTokenRepository.save(rf);
	}

	public RefreshToken findByToken(String token) {
		return this.refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new ResourceNotFoundException("khong thay token"));
	}

	public void deleteById(Long id) {
		this.refreshTokenRepository.deleteById(id);
	}

}
