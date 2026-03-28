package br.com.brevus.commerce_api.validation;

import br.com.brevus.commerce_api.exceptions.DuplicateRecordException;
import br.com.brevus.commerce_api.model.Category;
import br.com.brevus.commerce_api.repository.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryValidation {

    private final CategoryRepository categoryRepository;

    public CategoryValidation(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void validate(Category category){
        if(existsByName(category)){
            throw new DuplicateRecordException("Já existe uma categoria cadastrada com esse nome");
        }
    }

    public boolean existsByName(Category category){
        Optional<Category> categoryOptional = categoryRepository.findByName(category.getName());

        if (category.getId() == null){
            return categoryOptional.isPresent();
        }

        return categoryOptional.isPresent() && !category.getId().equals(categoryOptional.get().getId());
    }
}
