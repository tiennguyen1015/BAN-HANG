package duan.BAN_HANG.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import duan.BAN_HANG.model.Cart;
import duan.BAN_HANG.model.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findById(Long id);

	boolean existsByUserId(Long id);

	Optional<Cart> findByUserId(Long id);

	Optional<Cart> findByUser(User user);
}
