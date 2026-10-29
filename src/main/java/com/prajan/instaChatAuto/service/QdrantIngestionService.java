package com.prajan.instaChatAuto.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class QdrantIngestionService {

    private final RestClient restClient;
    private final String collection;
    private final String model;

    public QdrantIngestionService(
            @Value("${qdrant.url}") String url,
            @Value("${qdrant.api-key}") String apiKey,
            @Value("${qdrant.collection}") String collection,
            @Value("${qdrant.embedding-model}") String model) {

        this.restClient = RestClient.builder()
                .baseUrl(url)
                .defaultHeader("api-key", apiKey)
                .build();

        this.collection = collection;
        this.model = model;
    }

    public int ingestMeowMart() throws IOException {

        ClassPathResource resource = new ClassPathResource(
                "company-docs/meowmart-products.txt");

        String content = new String(
                resource.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8);

        List<String> chunks = splitIntoChunks(content);
        List<Map<String, Object>> points = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {
            String chunk = chunks.get(i);

            Map<String, Object> vector = Map.of(
                    "text", chunk,
                    "model", model
            );

            Map<String, Object> payload = Map.of(
                    "text", chunk,
                    "source", "meowmart-products.txt",
                    "company", "MeowMart",
                    "chunk_index", i
            );

            points.add(Map.of(
                    "id", UUID.randomUUID().toString(),
                    "vector", vector,
                    "payload", payload
            ));
        }

        Map<String, Object> request = Map.of(
                "points", points
        );

        restClient.put()
                .uri("/collections/{collection}/points?wait=true",
                        collection)
                .body(request)
                .retrieve()
                .toBodilessEntity();

        return points.size();
    }

    private List<String> splitIntoChunks(String text) {

        List<String> chunks = new ArrayList<>();

        // Keep initial chunks small for the model's
        // 256-token context window.
        String[] sections = text.split("(?m)(?=^## )");

        for (String section : sections) {
            String remaining = section.trim();

            while (!remaining.isEmpty()) {
                int end = Math.min(remaining.length(), 700);

                if (end < remaining.length()) {
                    int boundary = remaining.lastIndexOf(' ', end);
                    if (boundary > 0) {
                        end = boundary;
                    }
                }

                String chunk = remaining.substring(0, end).trim();

                if (!chunk.isEmpty()) {
                    chunks.add(chunk);
                }

                remaining = remaining.substring(end).trim();
            }
        }

        return chunks;
    }
}
