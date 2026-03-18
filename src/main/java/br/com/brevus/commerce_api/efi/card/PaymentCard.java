package br.com.brevus.commerce_api.efi.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentCard {

    @JsonProperty("credit_card")
    private CreditCard creditCard;
}
