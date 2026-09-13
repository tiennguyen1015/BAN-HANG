package duan.BAN_HANG.model;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "name không được để trống")
	private String name;
	@NotBlank(message = "email không được để trống")
	@Email(message = "Email không đúng định dạng")
	private String email;
	@NotBlank(message = "password không được để trống")
	private String password;
	@NotBlank(message = "address không được để trống")
	private String address;
	@NotBlank(message = "phone không được để trống")
	@Pattern(regexp = "^(0|\\+84)[0-9]{9}$", message = "Số điện thoại không đúng định dạng")
	private String phone;
	private Instant createdAt;
	private String imageUrl;

	@OneToOne(mappedBy = "user")
	private Cart cart;

	@OneToMany(mappedBy = "user")
	private List<Order> orders;

	@OneToMany(mappedBy = "user")
	private List<Review> reviews;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roel_id")
	private Role role;

	@ManyToMany
	@JoinTable(name = "user_product", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
	private List<Products> products;

	@PrePersist
	public void prePersist() {
		this.createdAt = Instant.now();
	}

}
