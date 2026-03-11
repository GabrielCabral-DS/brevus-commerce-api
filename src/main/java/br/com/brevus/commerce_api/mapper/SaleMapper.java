package br.com.brevus.commerce_api.mapper;

import br.com.brevus.commerce_api.dto.SaleRequestDTO;
import br.com.brevus.commerce_api.dto.SaleResponseDTO;
import br.com.brevus.commerce_api.model.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SaleItemMapper.class, PaymentMapper.class})
public interface SaleMapper {

    SaleRequestDTO toDTO(Sale sale);
    Sale toEntity(SaleRequestDTO dto);
    List<SaleResponseDTO> toDtoList(List<Sale> sales);
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "deliveryAddress.id", target = "addressId")
    SaleResponseDTO toResponseDto(Sale sale);
}
