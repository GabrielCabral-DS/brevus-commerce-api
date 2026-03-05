package br.com.brevus.commerce_api.mapper;


import br.com.brevus.commerce_api.dto.UserRequestDTO;
import br.com.brevus.commerce_api.dto.UsersResponseDTO;
import br.com.brevus.commerce_api.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserRequestDTO toDTO(User user);
    User toEntity(UserRequestDTO dto);
    List<UsersResponseDTO> toDtoList(List<User> users);
    UsersResponseDTO toDto(User user);
}
