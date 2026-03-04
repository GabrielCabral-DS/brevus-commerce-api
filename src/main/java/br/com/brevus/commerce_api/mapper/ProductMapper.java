package br.com.brevus.commerce_api.mapper;

import br.com.brevus.commerce_api.dto.ProductRequestDTO;
import br.com.brevus.commerce_api.dto.ProductResponseDTO;
import br.com.brevus.commerce_api.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductRequestDTO toDTO(Product product);
    Product toEntity(ProductRequestDTO dto);
    List<ProductRequestDTO> toDtoList(List<Product> products);
    List<Product> toEntityList(List<ProductRequestDTO> productRequestDTOList);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductResponseDTO toResponseDto(Product product);

    List<ProductResponseDTO> toResponseDtoList(List<Product> products);
}
