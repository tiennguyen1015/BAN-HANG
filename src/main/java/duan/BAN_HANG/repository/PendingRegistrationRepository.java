package duan.BAN_HANG.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import duan.BAN_HANG.model.PendingRegistration;

public interface PendingRegistrationRepository extends JpaRepository<PendingRegistration, Long> {
	Optional<PendingRegistration> findByEmail(String email);

	boolean existsByEmail(String email);
}
