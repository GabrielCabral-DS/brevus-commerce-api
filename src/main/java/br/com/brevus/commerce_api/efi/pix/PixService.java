package br.com.brevus.commerce_api.efi.pix;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PixService {

    private final JSONObject configuracoes;

    private static final String WEBHOOK_CHAVE = "9644bbe4-8fed-40ee-b58d-21460b8f7d7d";

    @Value("${app.pix.webhook.url}")
    private String WEBHOOK_URL;

    @Value("${app.pix.webhook.hmac.api}")
    private String HMAC;


    public PixService(final PixConfig pixConfig) {
        this.configuracoes = new JSONObject();
        this.configuracoes.put("client_id", pixConfig.clientId());
        this.configuracoes.put("client_secret", pixConfig.clientSecret());
        this.configuracoes.put("sandbox", pixConfig.sandbox());
        this.configuracoes.put("debug", pixConfig.debug());
        this.configuracoes.put("x-skip-mtls-checking", "false");


        try (InputStream certificadoStream = getClass()
                .getClassLoader()
                .getResourceAsStream(pixConfig.certificatePath())) {

            if (certificadoStream == null) {
                throw new IllegalStateException("Certificado não encontrado no path: " + pixConfig.certificatePath());
            }

            Path tempFile = Files.createTempFile("certificado", ".p12");
            Files.copy(certificadoStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            tempFile.toFile().deleteOnExit();

            this.configuracoes.put("certificate", tempFile.toAbsolutePath().toString());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar certificado: " + e.getMessage(), e);
        }
    }


    //Webhooks

    public JSONObject configurarWebhook() {
        HashMap<String, String> params = new HashMap<>();
        params.put("chave", WEBHOOK_CHAVE);

        JSONObject body = new JSONObject();
        body.put("webhookUrl", WEBHOOK_URL + HMAC);

        try {
            EfiPay efi = new EfiPay(configuracoes);
            JSONObject response = efi.call("pixConfigWebhook", params, body);
            System.out.println(response);
            return response;
        } catch (EfiPayException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
            throw new RuntimeException("Erro ao configurar webhook: " + e.getErrorDescription());
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado: " + e.getMessage());
        }
    }

    public JSONObject consultarWebhook(String chavePix) {
        HashMap<String, String> params = new HashMap<>();
        params.put("chave", chavePix);

        try {
            EfiPay efi = new EfiPay(configuracoes);
            return efi.call("pixDetailWebhook", params, new JSONObject());
        } catch (EfiPayException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
            throw new RuntimeException("Erro ao consultar webhook: " + e.getErrorDescription());
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao consultar webhook: " + e.getMessage());
        }
    }

    public JSONObject listarWebhooks() {
        HashMap<String, String> params = new HashMap<>();
        params.put("inicio", "2025-01-01T16:01:35Z");
        params.put("fim", "2030-06-30T16:01:35Z");

        try {
            EfiPay efi = new EfiPay(configuracoes);
            return efi.call("pixListWebhook", params, new JSONObject());
        } catch (EfiPayException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
            throw new RuntimeException("Erro ao listar webhooks: " + e.getErrorDescription());
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao listar webhooks: " + e.getMessage());
        }
    }

    public JSONObject deletarWebhook(String chavePix) {
        HashMap<String, String> params = new HashMap<>();
        params.put("chave", chavePix);

        try {
            EfiPay efi = new EfiPay(configuracoes);
            return efi.call("pixDeleteWebhook", params, new JSONObject());
        } catch (EfiPayException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
            throw new RuntimeException("Erro ao deletar webhook: " + e.getErrorDescription());
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao deletar webhook: " + e.getMessage());
        }
    }

    public JSONObject listarChavesPix(){
        return executarOperacao("pixListEvp", new HashMap<>());
    }

    public JSONObject criarChavePix(){
        return executarOperacao("pixCreateEvp", new HashMap<>());
    }

    public JSONObject deletarChavePix(final String chavePix){
        Map<String, String> params = new HashMap<>();
        params.put("chave", chavePix);
        return executarOperacao("pixDeleteEvp", params);
    }

    public JSONObject consultarPagamentoPorTxid(String txid) {
        Map<String, String> params = new HashMap<>();
        params.put("txid", txid);

        try {
            EfiPay efi = new EfiPay(configuracoes);
            JSONObject response = efi.call("pixDetailCharge", params, new JSONObject());
            log.info("Consulta por txid {}: {}", txid, response);
            return response;
        } catch (EfiPayException e) {
            log.error("Erro ao consultar pagamento: {} {}", e.getErrorDescription(), e.getError());
        } catch (Exception e) {
            log.error("Erro inesperado: {}", e.getMessage());
        }
        return null;
    }

    public JSONObject criarQrCode(PixRequestPayload pixRequestPayload) {

        var pagador = pixRequestPayload.pixInfoPayment();

        JSONObject body = new JSONObject();
        body.put("calendario", new JSONObject().put("expiracao", 300));
        body.put("devedor", new JSONObject()
                .put("cpf", pagador.getCpfPagador())
                .put("nome", pagador.getNomePagador()));
        body.put("valor", new JSONObject().put("original", pixRequestPayload.valor()));
        body.put("chave", pixRequestPayload.chave());

        JSONArray infoAdicionais = new JSONArray();
        infoAdicionais.put(
                new JSONObject().put("nome", pagador.getDescricaoProduto()).put("valor", pagador.getValorProduto()));
        body.put("infoAdicionais", infoAdicionais);

        try {
            EfiPay efi = new EfiPay(configuracoes);
            return efi.call("pixCreateImmediateCharge", new HashMap<>(), body);
        } catch (EfiPayException e) {
            log.error("Erro da API {} {}", e.getErrorDescription(), e.getError());
        } catch (Exception e) {
            log.error("Erro genérico {}", e.getMessage());
        }
        return null;
    }

    private JSONObject executarOperacao(String operacao, Map<String, String> params) {
        try {
            EfiPay efi = new EfiPay(configuracoes);
            JSONObject response = efi.call(operacao, params, new JSONObject());
            log.info("Resultado da operação {}: {}", operacao, response.toString(2));
            return response; // retorna a resposta diretamente se deu certo
        } catch (EfiPayException e) {
            log.error("Erro da API: {} ({})", e.getErrorDescription(), e.getError());
            JSONObject erro = new JSONObject();
            erro.put("erro", e.getErrorDescription());
            return erro;
        } catch (UnsupportedOperationException | JSONException operationException) {
            log.warn("Formato inválido no JSON: {}", operationException.getMessage());
            JSONObject erro = new JSONObject();
            erro.put("erro", "Formato inválido na resposta da API");
            return erro;
        } catch (Exception e) {
            log.error("Erro inesperado ao executar operação Pix", e);
            JSONObject erro = new JSONObject();
            erro.put("erro", "Erro inesperado: " + e.getMessage());
            return erro;
        }
    }
}