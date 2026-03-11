package br.com.brevus.commerce_api.dto;

import br.com.brevus.commerce_api.enums.State;
import java.util.UUID;

public record AddressResponseDTO (

        UUID id,
        UUID userId,

        String street,
        String number,
        String complement,
        String neighborhood,
        String  city,
        State state,
        String zipCode

) {
}
