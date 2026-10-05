package com.prajan.instaChatAuto.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class InstagramMessageService {

    @Value("${instagram.access-token}")
    private String accessToken;

    private final RestClient restClient;

    public InstagramMessageService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public void sendMessage(String recipientId, String message) {

        String url = "https://graph.instagram.com/v24.0/me/messages";

        Map<String, Object> body = Map.of(
                "recipient", Map.of("id", recipientId),
                "message", Map.of("text", message)
        );

        String response = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .retrieve()
                .body(String.class);

        System.out.println("Instagram API response: " + response);
    }
}