package br.com.brevus.commerce_api.mapper;

import br.com.brevus.commerce_api.dto.SaleRequestDTO;
import br.com.brevus.commerce_api.model.Sale;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    SaleRequestDTO toDTO(Sale sale);
    Sale toEntity(SaleRequestDTO dto);
    List<SaleRequestDTO> toDtoList(List<Sale> sales);
}
