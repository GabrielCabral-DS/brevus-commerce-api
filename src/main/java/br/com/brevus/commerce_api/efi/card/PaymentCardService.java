package br.com.brevus.commerce_api.efi.card;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

@Service
public class PaymentCardService {

    private final JSONObject configuracoes;

    public PaymentCardService(final GerencianetCredentials gerencianetCredentials) {
        this.configuracoes = new JSONObject();
        this.configuracoes.put("client_id", gerencianetCredentials.clientId());
        this.configuracoes.put("client_secret", gerencianetCredentials.clientSecret());
        this.configuracoes.put("sandbox", gerencianetCredentials.sandbox());
        this.configuracoes.put("debug", gerencianetCredentials.debug());

        try (
                InputStream certificadoStream = getClass()
                        .getClassLoader()
                        .getResourceAsStream(gerencianetCredentials.certificatePath())) {

            if (certificadoStream == null) {
                throw new IllegalStateException("Certificado não encontrado no path: " + gerencianetCredentials.certificatePath());
            }

            Path tempFile = Files.createTempFile("certificado", ".p12");
            Files.copy(certificadoStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            tempFile.toFile().deleteOnExit();

            this.configuracoes.put("certificate", tempFile.toAbsolutePath().toString());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar certificado: " + e.getMessage(), e);
        }
    }

    public JSONObject consultarCobranca(String chargeId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", chargeId);

        try {
            EfiPay efi = new EfiPay(configuracoes);
            JSONObject response = efi.call("detailCharge", params, new JSONObject());
            System.out.println(response);
            return response;
        } catch (EfiPayException e) {
            System.out.println(e.getCode());
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
            return new JSONObject()
                    .put("error", e.getError())
                    .put("description", e.getErrorDescription())
                    .put("code", e.getCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new JSONObject()
                    .put("error", "Erro interno")
                    .put("description", e.getMessage());
        }
    }

    public JSONObject notificacaoPagamentoCartao(String token) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);

        try {
            EfiPay efi = new EfiPay(configuracoes);
            JSONObject response = efi.call("getNotification", params, new JSONObject());
            System.out.println("Resposta da Efí (notificação): " + response);
            return response;
        } catch (EfiPayException e) {
            System.out.println("Erro EfiPay:");
            System.out.println("Code: " + e.getCode());
            System.out.println("Error: " + e.getError());
            System.out.println("Descrição: " + e.getErrorDescription());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }

        return null;
    }



    public JSONObject criarPagamentoCartao (OneStepChargeRequest request) {

        var pagador = request.payment();
        var itens = request.items();
        var empresaId = request.metadata().getCustomId();

        String paymentToken = pagador.getCreditCard().getPaymentToken();

        JSONArray itemsArray = new JSONArray();
        itens.forEach(item -> {
            JSONObject jsonItem = new JSONObject();
            jsonItem.put("name", item.getName());
            jsonItem.put("amount", item.getAmount());
            jsonItem.put("value", item.getValue());
            itemsArray.put(jsonItem);
        });
        //customer
        JSONObject customer = new JSONObject();
        customer.put("name", pagador.getCreditCard().getCustomer().getName());
        customer.put("cpf", pagador.getCreditCard().getCustomer().getCpf());
        customer.put("phone_number", pagador.getCreditCard().getCustomer().getPhoneNumber());
        customer.put("email", pagador.getCreditCard().getCustomer().getEmail());
        customer.put("birth", pagador.getCreditCard().getCustomer().getBirth());

        //address
        JSONObject billingAddress = new JSONObject();
        billingAddress.put("street", pagador.getCreditCard().getBillingAddressCard().getStreet());
        billingAddress.put("number", pagador.getCreditCard().getBillingAddressCard().getNumber());
        billingAddress.put("neighborhood", pagador.getCreditCard().getBillingAddressCard().getNeighborhood());
        billingAddress.put("zipcode", pagador.getCreditCard().getBillingAddressCard().getZipcode());
        billingAddress.put("city", pagador.getCreditCard().getBillingAddressCard().getCity());
        billingAddress.put("state", pagador.getCreditCard().getBillingAddressCard().getState());

        //notification URL
        JSONObject metadata = new JSONObject();
        metadata.put("notification_url", "https://gestao-back-end.onrender.com/api/card/webhook");
        metadata.put("custom_id", empresaId);


        JSONObject creditCard = new JSONObject();
        creditCard.put("installments", pagador.getCreditCard().getInstallments());
        creditCard.put("billing_address", billingAddress);
        creditCard.put("payment_token", paymentToken);
        creditCard.put("customer", customer);

        JSONObject payment = new JSONObject();
        payment.put("credit_card", creditCard);

        JSONObject body = new JSONObject();
        body.put("payment", payment);
        body.put("items", itemsArray);
        body.put("metadata", metadata);

        try {
            EfiPay efi = new EfiPay(configuracoes);
            JSONObject response = efi.call("createOneStepCharge", new HashMap<String,String>(), body);
            System.out.println(response);
            return response;
        }catch (EfiPayException e){
            System.out.println(e.getCode());
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
