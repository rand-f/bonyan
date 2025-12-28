package com.example.bnyan.Service;

import com.example.bnyan.Model.PaymentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class PaymentService {

    @Value("${MOYASSER_API_KEY}")
    private String apiKey;

    private static final String MOYASSER_API_URL =
            "https://api.moyasser.com/v1/payments";

    public ResponseEntity<String> processPayment(PaymentRequest paymentRequest) {

        String requestBody = String.format(
                "source[type]=card&" +
                        "source[name]=%s&" +
                        "source[number]=%s&" +
                        "source[cvc]=%s&" +
                        "source[month]=%s&" +
                        "source[year]=%s&" +
                        "amount=%d&" +
                        "currency=%s&" +
                        "callback_url=%s&" +
                        "description=%s",
                encode(paymentRequest.getName()),
                paymentRequest.getNumber(),
                paymentRequest.getCvc(),
                paymentRequest.getMonth(),
                paymentRequest.getYear(),
                (int) (paymentRequest.getAmount() * 100),
                paymentRequest.getCurrency(),
                encode(paymentRequest.getCallbackUrl()),
                encode(paymentRequest.getDescription())
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey, "");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        return new RestTemplate().exchange(
                MOYASSER_API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    private String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }
}
