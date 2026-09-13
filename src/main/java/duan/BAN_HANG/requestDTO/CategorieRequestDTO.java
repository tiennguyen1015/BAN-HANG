package duan.BAN_HANG.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategorieRequestDTO {

	private Long id;
	@NotBlank(message = "name không được để trống")
	private String name;
	@NotBlank(message = "mô tả không được để trống")
	private String description;
}
