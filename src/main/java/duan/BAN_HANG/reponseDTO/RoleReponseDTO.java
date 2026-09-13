package duan.BAN_HANG.reponseDTO;

import java.util.List;

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
public class RoleReponseDTO {

	private Long id;
	private String name;
	private String description;
	private List<UserReponseDTO> users;

}
