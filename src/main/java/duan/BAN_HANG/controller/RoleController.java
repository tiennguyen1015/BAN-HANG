package duan.BAN_HANG.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duan.BAN_HANG.helper.ApiResponse;
import duan.BAN_HANG.reponseDTO.PageResponse;
import duan.BAN_HANG.reponseDTO.RoleReponseDTO;
import duan.BAN_HANG.reponseFillterDTO.RoleFilterDTO;
import duan.BAN_HANG.requestDTO.RoleRequestDTO;
import duan.BAN_HANG.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("roles")
public class RoleController {

	private final RoleService roleService;

	@GetMapping()
	public ResponseEntity<?> getAllRoles(RoleFilterDTO roleFilterDTO, Pageable pageable) {
		Page<RoleReponseDTO> roleDTO = this.roleService.getAllRoles(roleFilterDTO, pageable);
		return ApiResponse.success(PageResponse.from(roleDTO), "Lấy danh sách vai trò thành công");
	}

	@PostMapping()
	public ResponseEntity<?> createRole(@Valid @RequestBody RoleRequestDTO requestDTO) {
		RoleReponseDTO role = this.roleService.createRole(requestDTO);
		return ApiResponse.success(role, "tạo thành công");
	}

	@PutMapping("{id}")
	public ResponseEntity<?> updateRole(@Valid @RequestBody RoleRequestDTO requestDTO, @PathVariable Long id) {
		RoleReponseDTO reponseDTO = this.roleService.updateRole(requestDTO, id);
		return ApiResponse.success(reponseDTO, "cập nhật thành công");
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		this.roleService.deleteRole(id);
		return ApiResponse.success("xóa thành công");
	}

}
