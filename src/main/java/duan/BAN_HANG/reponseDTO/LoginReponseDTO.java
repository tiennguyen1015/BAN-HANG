package duan.BAN_HANG.reponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginReponseDTO {
	private String accessToken;
	private String refreshToken;
	private String tokenType = "Bearer";
	private UserLogin user;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UserLogin {
		private Long id;
		private String name;
		private String role;

	}
}
