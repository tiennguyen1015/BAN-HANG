package duan.BAN_HANG.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import duan.BAN_HANG.exception.ResourceAlreadyExistsException;
import duan.BAN_HANG.exception.ResourceNotFoundException;
import duan.BAN_HANG.model.Role;
import duan.BAN_HANG.reponseDTO.RoleReponseDTO;
import duan.BAN_HANG.reponseFillterDTO.RoleFilterDTO;
import duan.BAN_HANG.repository.RoleRepository;
import duan.BAN_HANG.requestDTO.RoleRequestDTO;
import duan.BAN_HANG.specifition.RoleSpecifition;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;

//	public RoleReponseDTO convertToDTO(Role role) {
//
//		return RoleReponseDTO.builder().id(role.getId()).name(role.getName()).description(role.getDescription())
//				.users(role.getUsers().stream()
//						.map(user -> UserReponseDTO.builder().id(user.getId()).name(user.getName())
//								.email(user.getEmail()).address(user.getAddress()).phone(user.getPhone()).build())
//						.toList())
//				.build();
//	}

	public RoleReponseDTO convertToDTO(Role role) {

		return RoleReponseDTO.builder().id(role.getId()).name(role.getName()).description(role.getDescription())
//				.users(role.getUsers().stream()
//						.map(user -> UserReponseDTO.builder().id(user.getId()).name(user.getName())
//								.email(user.getEmail()).address(user.getAddress()).phone(user.getPhone()).build())
//						.toList())
				.build();
	}

	public Role convertToRole(RoleRequestDTO roleDTO) {
		Role role = new Role();
		role.setId(roleDTO.getId());
		role.setName(roleDTO.getName());
		role.setDescription(roleDTO.getDescription());
		return role;
	}

	public Page<RoleReponseDTO> getAllRoles(RoleFilterDTO roleFilterDTO, Pageable pageable) {
		Specification<Role> spec = Specification.allOf(RoleSpecifition.hasSearcKeyword(roleFilterDTO));
		return this.roleRepository.findAll(spec, pageable).map(this::convertToDTO);
	}

	public RoleReponseDTO createRole(RoleRequestDTO requestDTO) {
		if (this.roleRepository.existsByName(requestDTO.getName())) {
			throw new ResourceAlreadyExistsException("name đã tồn tại");
		}

		Role role = new Role();
		role.setName(requestDTO.getName());
		role.setDescription(requestDTO.getDescription());
		Role newRole = this.roleRepository.save(role);
		return convertToDTO(newRole);
	}

	public RoleReponseDTO updateRole(RoleRequestDTO requestDTO, Long id) {
		Role role = this.roleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("role không tồn tại"));
		if (this.roleRepository.existsByNameAndIdNot(requestDTO.getName(), id)) {
			throw new ResourceAlreadyExistsException("tên danh mục đã tồn tại");
		}

		role.setName(requestDTO.getName());
		role.setDescription(requestDTO.getDescription());
		Role newUpdate = this.roleRepository.save(role);
		return convertToDTO(newUpdate);
	}

	public void deleteRole(Long id) {
		Role role = this.roleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("role không tồn tại"));
		this.roleRepository.deleteById(id);
	}

}
