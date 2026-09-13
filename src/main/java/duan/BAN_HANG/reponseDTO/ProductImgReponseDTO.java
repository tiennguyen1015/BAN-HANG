package duan.BAN_HANG.reponseDTO;

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
public class ProductImgReponseDTO {
	private Long id;
	private String imageUrl;
	private ProductReponseDTO product;

}
