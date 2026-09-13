package duan.BAN_HANG.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Products {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "name không được để trống")
	private String name;
	@NotBlank(message = "description không được để trống")
	private String description;
	@NotNull(message = "gia không được để trống")
	private BigDecimal price;
	private Integer stock;
	private Instant createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Categories category;

//	@OneToMany(mappedBy = "product")
//	private List<ProductImage> images;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductImage> images = new ArrayList<>();
//	có thể dùng cách này để lấy danh sách ảnh ở bên productservice product.getImages().add(savedImage);

	@OneToMany(mappedBy = "product")
	private List<Review> reviews;

	@OneToMany(mappedBy = "product")
	private List<CartItem> cartItems;

	@OneToMany(mappedBy = "product")
	private List<OrderItem> orderItems;

	@ManyToMany(mappedBy = "products")
	private List<User> users;

	@PrePersist
	public void prePersist() {
		this.createdAt = Instant.now();
	}

}
