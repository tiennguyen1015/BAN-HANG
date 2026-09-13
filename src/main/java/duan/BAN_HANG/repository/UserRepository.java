package duan.BAN_HANG.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import duan.BAN_HANG.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	@Query("""
			SELECT u
			FROM User u
			WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   OR u.phone LIKE CONCAT('%', :keyword, '%')
			""")
	Page<User> search(@Param("keyword") String keyword, Pageable pageable);

}
