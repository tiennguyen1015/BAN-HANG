package duan.BAN_HANG.reponseFillterDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserFilterDTO {

	private String name;
	private String email;
	private String address;
	private String phone;
	private String role;

}
