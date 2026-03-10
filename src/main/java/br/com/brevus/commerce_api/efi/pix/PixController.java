package br.com.brevus.commerce_api.efi.pix;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(value = "/api/v1/pix", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payment Pix")
public record PixController(PixService pixService) {


    @Value("${app.pix.webhook.hmac.retorno.api}")
    private static String HMAC;

    @PostMapping
    @Operation(summary = "Payment", description = "Create Qr code by pix")
    public ResponseEntity<String> criarQrCode(@RequestBody PixRequestPayload pixRequestPayload){  //https://pix.sejaefi.com.br/cob/pagar/
        var response = this.pixService.criarQrCode(pixRequestPayload);
        return  ResponseEntity.ok().body(response.toString());
    }

    @PostMapping("/configure-webhook")
    @Operation(summary = "Configure", description = "Configure webhook")
    public ResponseEntity<String> configurarWebhook() {
        try {
            JSONObject response = pixService.configurarWebhook();
            return ResponseEntity.ok(response.toString(2));
        } catch (Exception e) {
            log.error("Erro ao configurar webhook", e);
            return ResponseEntity.status(500).body("Erro ao configurar webhook: " + e.getMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receberWebhook(@RequestBody String payload,
                                                 @RequestParam(value = "hmac", required = false) String hmac) {
        // Aqui você pode validar o HMAC, se quiser
        System.out.println("Webhook recebido (HMAC=" + hmac + "): " + payload);

        // Retorne 200 OK para a Efí saber que recebeu
        return ResponseEntity.ok("Webhook processado com sucesso");
    }

    @GetMapping("/list")
    @Operation(summary = "List", description = "List key Pix.")
    public ResponseEntity<String> listarChavesPix(){
        var response = this.pixService.listarChavesPix();
        return ResponseEntity.ok().body(response.toString());
    }

    @GetMapping
    @Operation(summary = "Register", description = "Register a new key pix.")
    public ResponseEntity<String> criarChavePix(){
        var response = this.pixService.criarChavePix();
        return ResponseEntity.ok().body(response.toString());
    }

    @GetMapping("/search/{txId}")
    @Operation(summary = "Search", description = "Search payment pix.")
    public ResponseEntity<String> consultarPagamentoPorTxid(@PathVariable(value = "txId") String txId) {
        var response = this.pixService.consultarPagamentoPorTxid(txId);
        return ResponseEntity.ok().body(response.toString());
    }

    @GetMapping("/search-webhook/{key}")
    @Operation(summary = "Search", description = "Search Webhook.")
    public ResponseEntity<String> searchWebhookToKey(@PathVariable(value = "key") String key) {
        JSONObject response = pixService.consultarWebhook(key);
        return ResponseEntity.ok(response.toString());
    }

    @GetMapping("/list-webhook")
    @Operation(summary = "List", description = "List Webhook.")
    public ResponseEntity<String> listWebhook() {
        JSONObject response = pixService.listarWebhooks();
        return ResponseEntity.ok(response.toString());
    }

    @DeleteMapping("/delete-webhook/{key}")
    @Operation(summary = "Delete", description = "Delete Webhook.")
    public ResponseEntity<Void> deleteWebhook(@PathVariable(value = "key") String key) {
        pixService.deletarWebhook(key);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete", description = "Delete key pix")
    public ResponseEntity<String> deletarChavePix(@RequestParam("chavePix") String chavePix) {
        var response = this.pixService.deletarChavePix(chavePix);
        return ResponseEntity.ok()
                .body(response.toString());
    }
}
