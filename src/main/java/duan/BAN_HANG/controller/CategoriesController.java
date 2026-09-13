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
import duan.BAN_HANG.reponseDTO.CategorieReponseDTO;
import duan.BAN_HANG.reponseDTO.PageResponse;
import duan.BAN_HANG.requestDTO.CategorieRequestDTO;
import duan.BAN_HANG.service.CategorieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoriesController {

	private final CategorieService categorieService;

	@GetMapping
	public ResponseEntity<?> getAllCategory(String searchKeyword, Pageable pageable) {
		Page<CategorieReponseDTO> categorieReponseDTO = categorieService.getAllCategories(searchKeyword, pageable);
		return ApiResponse.success(PageResponse.from(categorieReponseDTO), "Lấy danh sách category thành công");
	}

	@PostMapping()
	public ResponseEntity<?> createCategory(@Valid @RequestBody CategorieRequestDTO categoriesRequestDTO) {
		CategorieReponseDTO categorieReponseDTO = this.categorieService.createCategories(categoriesRequestDTO);
		return ApiResponse.success(categorieReponseDTO, "Tạo category thành công");
	}

	@GetMapping("{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
		CategorieReponseDTO categorieReponseDTO = this.categorieService.getCategoryById(id);
		return ApiResponse.success(categorieReponseDTO, "Lấy category thành công");
	}

	@PutMapping("{id}")
	public ResponseEntity<?> updateCategory(@Valid @RequestBody CategorieRequestDTO categoriesRequestDTO,
			@PathVariable Long id) {
		this.categorieService.updateCategory(categoriesRequestDTO, id);
		return ApiResponse.success("Cập nhật category thành công");
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
		this.categorieService.deleteCategory(id);
		return ApiResponse.success("Xóa category thành công");
	}

}
