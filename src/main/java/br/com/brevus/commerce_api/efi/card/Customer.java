package br.com.brevus.commerce_api.efi.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Customer {

    private String name;
    private String cpf;
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String birth;

    @JsonProperty("address")
    private AddressCard addressCard;
}