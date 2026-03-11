package br.com.brevus.commerce_api.mapper;

import br.com.brevus.commerce_api.dto.AddressRequestDTO;
import br.com.brevus.commerce_api.dto.AddressResponseDTO;
import br.com.brevus.commerce_api.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressRequestDTO toDTO(Address address);
    Address toEntity(AddressRequestDTO addressRequestDTO);

    @Mapping(source = "user.id", target = "userId")
    AddressResponseDTO toResponseDto(Address address);
    List<AddressResponseDTO> toDtoList(List<Address> addressList);
}
