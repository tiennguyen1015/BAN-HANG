package duan.BAN_HANG.requestDTO;

import jakarta.validation.constraints.NotBlank;
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
public class RoleRequestDTO {

	private Long id;
	@NotBlank(message = "name không được để trống")
	private String name;
	@NotBlank(message = "description không được để trống")
	private String description;

}
