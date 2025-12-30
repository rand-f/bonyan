package com.example.bnyan.Service;

import com.example.bnyan.DTO.CardDTO;
import com.example.bnyan.DTO.PaymentRequestDTO;
import com.example.bnyan.DTO.PaymentResultDTO;
import com.example.bnyan.DTO.PaymentStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${MOYASSER_API_KEY}")
    private String apiKey;

    private static final String MOYASAR_API_URL =
            "https://api.moyasar.com/v1/payments";

    private final RestTemplate restTemplate;

    public PaymentResultDTO createPayment(PaymentRequestDTO request) {
         try {
             // Ensure amount ends with 0
            int amount = request.getAmount();
            if (amount % 10 != 0) {
                amount = ((amount + 9) / 10) * 10;
            }
            int amountInHalala = amount * 100;

            CardDTO card = request.getSource();

            String description = URLEncoder.encode(
                    request.getDescription(), StandardCharsets.UTF_8
            );
            String callbackUrl = URLEncoder.encode(
                    request.getCallbackUrl(), StandardCharsets.UTF_8
            );

            String body = String.format(
                    "amount=%d&currency=SAR&description=%s&callback_url=%s&" +
                            "source[type]=card&source[name]=%s&source[number]=%s&" +
                            "source[cvc]=%s&source[month]=%s&source[year]=%s",
                    amountInHalala,
                    description,
                    callbackUrl,
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

            return new PaymentResultDTO(
                    response.getStatusCode().is2xxSuccessful(),
                    jsonNode.get("id").asText(),
                    jsonNode.get("source").get("transaction_url").asText(),
                    "Payment initiated successfully"
            );

        } catch (Exception e) {
            return new PaymentResultDTO(
                    false,
                    null,
                    null,
                    "Payment failed: " + e.getMessage()
            );
        }
    }

    public PaymentStatusDTO getPaymentStatus(String paymentId) {
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
            return mapper.readValue(response.getBody(), PaymentStatusDTO.class);

        } catch (Exception e) {
            PaymentStatusDTO errorStatus = new PaymentStatusDTO();
            errorStatus.setId(paymentId);
            errorStatus.setStatus("error");
            errorStatus.setDescription(
                    "Failed to get payment status: " + e.getMessage()
            );
            return errorStatus;
        }
    }
}