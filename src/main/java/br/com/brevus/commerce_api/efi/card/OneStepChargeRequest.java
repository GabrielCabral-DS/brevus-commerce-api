package br.com.brevus.commerce_api.efi.card;

import java.util.List;

public record OneStepChargeRequest(PaymentCard payment, List<Item> items, Metadata metadata) {

}
