package br.com.brevus.commerce_api.mapper;

import br.com.brevus.commerce_api.dto.LoginHistoryResponseDTO;
import br.com.brevus.commerce_api.model.LoginHistory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoginHistoryMapper {

    List<LoginHistoryResponseDTO> toDtoList(List<LoginHistory> historyList);
}
