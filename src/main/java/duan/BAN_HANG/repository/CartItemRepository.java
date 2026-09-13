package duan.BAN_HANG.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import duan.BAN_HANG.model.Cart;
import duan.BAN_HANG.model.CartItem;
import duan.BAN_HANG.model.Products;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findByProductId(Long id);

	Optional<CartItem> findByCartAndProduct(Cart cart, Products product);
}
