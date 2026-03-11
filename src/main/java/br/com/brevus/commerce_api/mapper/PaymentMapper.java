package br.com.brevus.commerce_api.mapper;

import br.com.brevus.commerce_api.dto.PaymentRequestDTO;
import br.com.brevus.commerce_api.dto.PaymentResponseDTO;
import br.com.brevus.commerce_api.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "sale.id", target = "saleId")
    PaymentResponseDTO toResponseDTO(Payment payment);

    PaymentRequestDTO toDto(Payment payment);
    Payment toEntity(PaymentRequestDTO dto);
    List<PaymentResponseDTO> toDtoList(List<Payment> paymentList);

}
