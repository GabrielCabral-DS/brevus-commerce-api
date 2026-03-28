package br.com.brevus.commerce_api.efi.card;

import br.com.brevus.commerce_api.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
@Tag(name = "Payment card")
public class CardController {

    private final PaymentCardService paymentCardService;
    private final PaymentService paymentService;
    private static final Logger logger = LoggerFactory.getLogger(CardController.class);


    @PostMapping
    @Operation(summary = "register", description = "Register a new payment card")
    public ResponseEntity<String> criarPagamento(@RequestBody OneStepChargeRequest payload) {
        var response = this.paymentCardService.criarPagamentoCartao(payload);
        return ResponseEntity.ok().body(response.toString());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar", description = "Consultar cobrança.")
    public ResponseEntity<String> consultarCobranca(@PathVariable("id") String chargeId) {
        var response = this.paymentCardService.consultarCobranca(chargeId);
        return ResponseEntity.ok(response.toString());
    }

    @GetMapping("/notification/{notification}")
    @Operation(summary = "Notification", description = "Get notification payment")
    public ResponseEntity<String> notificacaoPagamentoCartao(@PathVariable("notification") String notificationToken) {
        var response = this.paymentCardService.notificacaoPagamentoCartao(notificationToken);
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/webhook")
    @Operation(summary = "Get", description = "Get Webhook.")
    public ResponseEntity<String> receberWebhook(@RequestBody String payload, HttpServletRequest request) {

        try {
            String notificationToken = Arrays.stream(payload.split("&"))
                    .filter(s -> s.startsWith("notification="))
                    .map(s -> s.replace("notification=", ""))
                    .findFirst()
                    .orElse(null);

            if (notificationToken == null) {
                logger.warn("Notification token não encontrado no payload");
                return ResponseEntity.badRequest().body("Notification token não encontrado");
            }

            JSONObject eventos = paymentCardService.notificacaoPagamentoCartao(notificationToken);
            if (eventos == null || !eventos.has("data")) {
                logger.warn("Nenhum evento retornado da Efí");
                return ResponseEntity.ok("Nenhum evento a processar");
            }

            JSONArray dataArray = eventos.getJSONArray("data");
            if (dataArray.isEmpty()) {
                logger.warn("Lista de eventos vazia");
                return ResponseEntity.ok("Nenhum evento a processar");
            }

            JSONObject ultimoEvento = dataArray.getJSONObject(dataArray.length() - 1);
            JSONObject identifiers = ultimoEvento.getJSONObject("identifiers");
            String chargeId = identifiers.get("charge_id").toString();

            JSONObject pagamento = paymentCardService.consultarCobranca(chargeId);
            if (pagamento != null && pagamento.has("data")) {
                JSONObject data = pagamento.getJSONObject("data");

                BigDecimal valor = BigDecimal.valueOf(data.getDouble("total") / 100);
                boolean statusPagamento = "paid".equalsIgnoreCase(data.getString("status"));

                String customId = data.optString("custom_id", null);
                UUID saleId = UUID.fromString(customId);

                if (statusPagamento) {
                    paymentService.savePayment(saleId, valor, chargeId);
                    logger.info("Pagamento aprovado e salvo no banco.");
                } else {
                    logger.info("Pagamento NÃO aprovado, nada será salvo.");
                }
            }

            return ResponseEntity.ok("Webhook processado com sucesso");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao processar webhook");
        }
    }
}
