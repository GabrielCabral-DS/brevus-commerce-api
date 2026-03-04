package br.com.brevus.commerce_api.mapper;


import br.com.brevus.commerce_api.dto.CategoryRequestDTO;
import br.com.brevus.commerce_api.model.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryRequestDTO toDTO (Category category);
    Category toEntity(CategoryRequestDTO dto);
    List<CategoryRequestDTO> toDtoList(List<Category> categories);
}
