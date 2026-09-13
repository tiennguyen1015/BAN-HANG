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
public class RoleFilterDTO {
	private Long id;
	private String searchKeyword;
	private String name;
	private String description;
}
