package br.com.brevus.commerce_api.efi.card;

import lombok.Data;

@Data
public class AddressCard {

    private String street;
    private String number;
    private String neighborhood;
    private String zipcode;
    private String city;
    private String state;

}