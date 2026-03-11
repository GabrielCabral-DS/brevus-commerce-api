package br.com.brevus.commerce_api.mapper;

import br.com.brevus.commerce_api.dto.SaleItemRequestDTO;
import br.com.brevus.commerce_api.dto.SaleItemResponseDTO;
import br.com.brevus.commerce_api.model.SaleItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleItemMapper {

    @Mapping(source = "sale.id", target = "saleId")
    @Mapping(source = "product.id", target = "productId")
    SaleItemResponseDTO toResponseDto(SaleItem saleItem);


    SaleItemRequestDTO toDTO(SaleItem saleItem);
    SaleItem toEntity(SaleItemRequestDTO saleItemRequestDTO);
    List<SaleItemResponseDTO> toDtoList(List<SaleItem> saleItemList);
}
