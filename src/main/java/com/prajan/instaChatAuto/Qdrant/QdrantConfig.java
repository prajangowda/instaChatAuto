package com.prajan.instaChatAuto.Qdrant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class QdrantConfig {

    @Bean
    public RestClient qdrantRestClient(
            @Value("${qdrant.url}") String url,
            @Value("${qdrant.api-key}") String apiKey) {

        return RestClient.builder()
                .baseUrl(url)
                .defaultHeader("api-key", apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}

