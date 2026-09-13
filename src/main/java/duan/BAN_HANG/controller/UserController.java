package duan.BAN_HANG.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duan.BAN_HANG.helper.ApiResponse;
import duan.BAN_HANG.model.User;
import duan.BAN_HANG.reponseDTO.PageResponse;
import duan.BAN_HANG.reponseDTO.UserReponseDTO;
import duan.BAN_HANG.requestDTO.UserRequestDTO;
import duan.BAN_HANG.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

//	@GetMapping
//	public ResponseEntity<?> getAlUsers(UserFilterDTO userFilter, Pageable pageable) {
//		Page<UserReponseDTO> users = this.userService.feachAllUsers(userFilter, pageable);
//		return ApiResponse.success(PageResponse.from(users), "Lấy danh sách người dùng thành công");
//	}

	@GetMapping
	public ResponseEntity<?> getAlUsers(String searchKeyword, Pageable pageable) {
		Page<UserReponseDTO> users = this.userService.feachAllUsers(searchKeyword, pageable);
		return ApiResponse.success(PageResponse.from(users), "Lấy danh sách người dùng thành công");
	}

	@PostMapping
	public ResponseEntity<ApiResponse<UserReponseDTO>> createUser(@Valid @RequestBody User user) {
		UserReponseDTO newUser = this.userService.createUser(user);
		return ApiResponse.success(newUser, "Tạo người dùng thành công");
	}

	@GetMapping("{id}")
	public ResponseEntity<ApiResponse<UserReponseDTO>> userById(@PathVariable Long id) {
		UserReponseDTO user = this.userService.getUserById(id);
		return ApiResponse.success(user, "lấy thông tin người dùng thành công");
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserReponseDTO>> getMyProfile(Authentication authentication) {
		UserReponseDTO user = this.userService.getUserByEmail();
		return ApiResponse.success(user, "lấy thông tin người dùng thành công");
	}

	@PutMapping("{id}")
	public ResponseEntity<ApiResponse<String>> updateUser(@Valid @RequestBody UserRequestDTO userDto,
			@PathVariable Long id) {
		this.userService.updateUser(userDto, id);
		return ApiResponse.success("Cập nhật người dùng thành công");
	}

	@DeleteMapping("{id}")
	public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
		this.userService.deleteUser(id);
		return ApiResponse.success("Xóa người dùng thành công");
	}

}
