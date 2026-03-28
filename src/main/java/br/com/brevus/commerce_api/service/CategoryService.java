package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.CategoryReponseDTO;
import br.com.brevus.commerce_api.dto.CategoryRequestDTO;
import br.com.brevus.commerce_api.exceptions.ResourceNotFoundException;
import br.com.brevus.commerce_api.mapper.CategoryMapper;
import br.com.brevus.commerce_api.model.Category;
import br.com.brevus.commerce_api.repository.CategoryRepository;
import br.com.brevus.commerce_api.validation.CategoryValidation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryValidation categoryValidation;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, CategoryValidation categoryValidation) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.categoryValidation = categoryValidation;
    }

    public Category register(CategoryRequestDTO dto){
        categoryValidation.validate(categoryMapper.toEntity(dto));
        return categoryRepository.save(categoryMapper.toEntity(dto));
    }

    public List<CategoryReponseDTO> listAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDtoList(categories);
    }

    public Category update(UUID id, CategoryRequestDTO dto){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Categoria não encontrada"));
        category.setName(dto.name());
        categoryValidation.validate(category);
        return categoryRepository.save(category);
    }

    public void delete(UUID id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Categoria não encontrada"));
        categoryRepository.delete(category);
    }
}
