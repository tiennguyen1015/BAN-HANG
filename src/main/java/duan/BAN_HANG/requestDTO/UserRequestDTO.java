package duan.BAN_HANG.requestDTO;

import duan.BAN_HANG.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserRequestDTO {

	@NotBlank(message = "name không được để trống")
	private String name;
	@NotBlank(message = "email không được để trống")
	private String email;
//	@NotBlank(message = "password không được để trống")
	private String password;
	@NotBlank(message = "address không được để trống")
	private String address;
	@NotBlank(message = "phone không được để trống")
	private String phone;
	@NotNull(message = "role không được để trống")
	private Role role;

}
