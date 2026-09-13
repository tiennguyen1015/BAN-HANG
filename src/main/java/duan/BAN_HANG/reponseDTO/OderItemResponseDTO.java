package duan.BAN_HANG.reponseDTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OderItemResponseDTO {
	private Long id;
	private Integer quantity;
	private BigDecimal price;
	private ProductReponseDTO product;
	private OderResponseDTO order;

}
