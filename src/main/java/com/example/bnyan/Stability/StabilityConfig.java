package com.example.bnyan.Stability;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class StabilityConfig {

    @Value("${stability.api.key}")
    private String apiKey;

    @Bean
    public WebClient stabilityWebClient() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer ->
                        configurer.defaultCodecs().maxInMemorySize(9 * 1024 * 1024) // 9 MB
                )
                .build();

        return WebClient.builder()
                .baseUrl("https://api.stability.ai")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .exchangeStrategies(strategies)
                .build();
    }
}
