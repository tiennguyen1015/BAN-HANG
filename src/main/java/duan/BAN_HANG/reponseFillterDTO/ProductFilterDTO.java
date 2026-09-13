package duan.BAN_HANG.reponseFillterDTO;

import java.math.BigDecimal;

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
public class ProductFilterDTO {
	private String searchKeyword;
	private BigDecimal price;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private Integer stock;
}
