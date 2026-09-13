package duan.BAN_HANG.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import duan.BAN_HANG.exception.ResourceNotFoundException;
import duan.BAN_HANG.model.Categories;
import duan.BAN_HANG.reponseDTO.CategorieReponseDTO;
import duan.BAN_HANG.repository.CategorieRepository;
import duan.BAN_HANG.requestDTO.CategorieRequestDTO;
import duan.BAN_HANG.specifition.CategorieSpecifition;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategorieService {

	private final CategorieRepository categorieRepository;

	public CategorieReponseDTO convertCategorieToDTO(Categories categories) {
		CategorieReponseDTO dto = CategorieReponseDTO.builder().id(categories.getId()).name(categories.getName())
				.description(categories.getDescription()).build();
		return dto;
	}

	public Categories convertDTOToCategorie(CategorieRequestDTO dto) {
		Categories categories = new Categories();
		categories.setId(dto.getId());
		categories.setName(dto.getName());
		categories.setDescription(dto.getDescription());
		return categories;
	}

	public Page<CategorieReponseDTO> getAllCategories(String searchKeyword, Pageable pageable) {
		Specification<Categories> spec = Specification.allOf(CategorieSpecifition.hasSearchKeyword(searchKeyword));
		return this.categorieRepository.findAll(spec, pageable).map(this::convertCategorieToDTO);

	}

	public CategorieReponseDTO createCategories(CategorieRequestDTO categoriesRequestDTO) {
		if (this.categorieRepository.existsByName(categoriesRequestDTO.getName())) {
			throw new ResourceNotFoundException("tên danh mục đã tồn tại");
		}
		Categories categories = this.categorieRepository.save(convertDTOToCategorie(categoriesRequestDTO));
		return convertCategorieToDTO(categories);
	}

	public CategorieReponseDTO getCategoryById(Long id) {
		Categories categories = this.categorieRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id: " + id));
		return convertCategorieToDTO(categories);
	}

	public void updateCategory(CategorieRequestDTO categoriesRequestDTO, Long id) {
		Categories categories = this.categorieRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id: " + id));
		categories.setName(categoriesRequestDTO.getName());
		categories.setDescription(categoriesRequestDTO.getDescription());
		this.categorieRepository.save(categories);
	}

	public void deleteCategory(Long id) {
		Categories categories = this.categorieRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id: " + id));
		this.categorieRepository.deleteById(id);
	}

}
