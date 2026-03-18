package br.com.brevus.commerce_api.efi.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Metadata {

    @JsonProperty("custom_id")
    private String customId;
}
