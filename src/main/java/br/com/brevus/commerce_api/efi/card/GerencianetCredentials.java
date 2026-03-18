package br.com.brevus.commerce_api.efi.card;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.pix")
public record GerencianetCredentials (String clientId, String clientSecret, boolean sandbox, boolean debug, String certificatePath) {
}