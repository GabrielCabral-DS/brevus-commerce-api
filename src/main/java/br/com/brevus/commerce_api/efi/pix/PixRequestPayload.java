package br.com.brevus.commerce_api.efi.pix;

public record PixRequestPayload (String chave, String valor, PixInfoPaymentDTO pixInfoPayment) {

}