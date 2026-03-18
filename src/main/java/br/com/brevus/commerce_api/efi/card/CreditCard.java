package br.com.brevus.commerce_api.efi.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreditCard {
    @JsonProperty("payment_token")
    private String paymentToken;

    private Integer installments;

    @JsonProperty("billing_address")
    private AddressCard billingAddressCard;

    private Customer customer;
}
