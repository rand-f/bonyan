package com.example.bnyan.Service;

import com.example.bnyan.Model.Card;
import com.example.bnyan.Model.PaymentRequest;
import com.example.bnyan.Model.PaymentResult;
import com.example.bnyan.Model.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class PaymentService {


    @Value("${moyasar.api.key}")
    private String apiKey;

    private static final String MOYASAR_API_URL =
            "https://api.moyasar.com/v1/payments";

    private final RestTemplate restTemplate = new RestTemplate();
    public PaymentResult createPayment(PaymentRequest request) {
        try {
            // Ensure amount ends with 0
            int amount = request.getAmount();
            if (amount % 10 != 0) {
                amount = ((amount + 9) / 10) * 10; // round up to nearest 10
            }
            int amountInHalala = amount * 100;

            Card card = request.getSource();

            String body = String.format(
                    "amount=%d&currency=SAR&description=%s&callback_url=%s&" +
                            "source[type]=card&source[name]=%s&source[number]=%s&" +
                            "source[cvc]=%s&source[month]=%s&source[year]=%s",
                    amountInHalala,
                    request.getDescription(),
                    request.getCallbackUrl(),
                    card.getName(),
                    card.getNumber(),
                    card.getCvc(),
                    card.getMonth(),
                    card.getYear()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(apiKey, "");

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    MOYASAR_API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.getBody());

            PaymentResult result = new PaymentResult();
            result.setSuccess(response.getStatusCode().is2xxSuccessful());
            result.setPaymentId(jsonNode.get("id").asText());
            result.setTransactionUrl(jsonNode.get("source").get("transaction_url").asText());
            result.setMessage("Payment initiated successfully");

            return result;

        } catch (Exception e) {
            PaymentResult result = new PaymentResult();
            result.setSuccess(false);
            result.setMessage("Payment failed: " + e.getMessage());
            return result;
        }
    }

    public PaymentStatus getPaymentStatus(String paymentId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(apiKey, "");
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    MOYASAR_API_URL + "/" + paymentId,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody(), PaymentStatus.class);

        } catch (Exception e) {
            // Return a PaymentStatus with error info
            PaymentStatus errorStatus = new PaymentStatus();
            errorStatus.setId(paymentId);
            errorStatus.setStatus("error");
            errorStatus.setDescription("Failed to get payment status: " + e.getMessage());
            return errorStatus;
        }
    }


}