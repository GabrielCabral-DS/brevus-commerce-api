package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.CategoryRequestDTO;
import br.com.brevus.commerce_api.mapper.CategoryMapper;
import br.com.brevus.commerce_api.model.Category;
import br.com.brevus.commerce_api.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Category register(CategoryRequestDTO dto){
        return categoryRepository.save(categoryMapper.toEntity(dto));
    }

    public List<CategoryRequestDTO> listAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDtoList(categories);
    }

    public Category update(UUID id, CategoryRequestDTO dto){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Categoria não encontrada"));
        category.setName(dto.name());
        return categoryRepository.save(category);
    }

    public void delete(UUID id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Categoria não encontrada"));
        categoryRepository.delete(category);
    }
}
