package duan.BAN_HANG.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import duan.BAN_HANG.exception.ResourceAlreadyExistsException;
import duan.BAN_HANG.exception.ResourceNotFoundException;
import duan.BAN_HANG.model.Role;
import duan.BAN_HANG.model.User;
import duan.BAN_HANG.reponseDTO.UserReponseDTO;
import duan.BAN_HANG.repository.RoleRepository;
import duan.BAN_HANG.repository.UserRepository;
import duan.BAN_HANG.requestDTO.UserRequestDTO;
import duan.BAN_HANG.specifition.UserSpecifition;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;

	public UserReponseDTO convertUserToDTO(User user) {
		return UserReponseDTO.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
				.address(user.getAddress()).phone(user.getPhone()).imageUrl(user.getImageUrl())
				.role(new UserReponseDTO.Role(user.getRole().getId(), user.getRole().getName(),
						user.getRole().getDescription()))
				.cart(user.getCart() == null ? null
						: new UserReponseDTO.Cart(user.getCart().getId(), user.getCart().getCreatedAt()))

				.build();
	}

//	public Page<UserReponseDTO> feachAllUsers(UserFilterDTO userFilter, Pageable pageable) {
//
//		Specification<User> spec = Specification.allOf(UserSpecifition.hasName(userFilter.getName()),
//				UserSpecifition.hasEmail(userFilter.getEmail()), UserSpecifition.hasAddress(userFilter.getAddress()),
//				UserSpecifition.hasPhone(userFilter.getPhone()), UserSpecifition.hasRole(userFilter.getRole()));
//
//		return this.userRepository.findAll(spec, pageable).map(this::convertUserToDTO);
//	}

	public Page<UserReponseDTO> feachAllUsers(String searchKeyword, Pageable pageable) {

//		Specification<User> spec = Specification.allOf(UserSpecifition.hasName(userFilter.getName()),
//				UserSpecifition.hasEmail(userFilter.getEmail()), UserSpecifition.hasAddress(userFilter.getAddress()),
//				UserSpecifition.hasPhone(userFilter.getPhone()), UserSpecifition.hasRole(userFilter.getRole()));

		Specification<User> spec = Specification.allOf(UserSpecifition.hasSearchKeyword(searchKeyword));

		return this.userRepository.findAll(spec, pageable).map(this::convertUserToDTO);
	}

	public UserReponseDTO createUser(User user) {

		if (this.userRepository.existsByEmail(user.getEmail())) {
			throw new ResourceAlreadyExistsException("mail đã tồn tại");
		}

		if (user.getRole() == null || user.getRole().getId() == null) {
			throw new ResourceAlreadyExistsException("Bạn chưa chọn quyền hạn");
		}

		Role role = roleRepository.findById(user.getRole().getId())
				.orElseThrow(() -> new ResourceAlreadyExistsException("Quyền hạn không tồn tại"));

		String hassPassword = this.passwordEncoder.encode(user.getPassword());
		user.setRole(role);
		user.setPassword(hassPassword);
		User newUser = this.userRepository.save(user);
		return convertUserToDTO(newUser);
	}

	public User findByEmail(String email) {
		User user = this.userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Thông tin đăng nhập không chính xác"));
		return user;
	}

	public UserReponseDTO findByUsername(String username) {
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("Thông tin đăng nhập không chính xác"));
		return convertUserToDTO(user);
	}

	public UserReponseDTO getUserById(Long id) {
		User user = this.userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		return convertUserToDTO(user);
	}

	public UserReponseDTO getUserByEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
		return convertUserToDTO(user);
	}

	public void updateUser(UserRequestDTO user, Long id) {

		User existingUser = this.userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		existingUser.setName(user.getName());
		existingUser.setEmail(user.getEmail());
//		existingUser.setPassword(user.getPassword());
//		String hassPassword = this.passwordEncoder.encode(user.getPassword());
//		existingUser.setPassword(hassPassword);

		if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
			existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
		}

		existingUser.setAddress(user.getAddress());
		existingUser.setPhone(user.getPhone());
		existingUser.setRole(user.getRole());
		this.userRepository.save(existingUser);
	}

	public void deleteUser(Long id) {
		User existingUser = this.userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		this.userRepository.delete(existingUser);
	}

	// đăng ký tài khoản
	public UserReponseDTO Register(User user) {

		if (this.userRepository.existsByEmail(user.getEmail())) {
			throw new ResourceAlreadyExistsException("mail đã tồn tại");
		}

		Role role = roleRepository.findById(2L)
				.orElseThrow(() -> new ResourceAlreadyExistsException("Quyền hạn không tồn tại"));

		String hassPassword = this.passwordEncoder.encode(user.getPassword());
		user.setRole(role);
		user.setPassword(hassPassword);
		User newUser = this.userRepository.save(user);
		return convertUserToDTO(newUser);
	}

}
