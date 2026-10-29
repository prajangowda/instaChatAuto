package com.prajan.instaChatAuto.Qdrant;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@Service
public class QdrantRetrievalService {

    private final RestClient qdrantClient;
    private final String collection;
    private final String embeddingModel;

    public QdrantRetrievalService(
            RestClient qdrantRestClient,
            @Value("${qdrant.collection}") String collection,
            @Value("${qdrant.embedding-model}") String embeddingModel) {

        this.qdrantClient = qdrantRestClient;
        this.collection = collection;
        this.embeddingModel = embeddingModel;
    }

    public List<String> retrieveChunks(String userQuery) {

        var request = java.util.Map.of(
                "query", java.util.Map.of(
                        "text", userQuery,
                        "model", embeddingModel
                ),
                "limit", 3,
                "with_payload", true
        );

        JsonNode response = qdrantClient.post()
                .uri("/collections/{collection}/points/query",
                        collection)
                .body(request)
                .retrieve()
                .body(JsonNode.class);

        List<String> chunks = new ArrayList<>();

        if (response == null) {
            return chunks;
        }

        JsonNode points = response.path("result").path("points");

        for (JsonNode point : points) {
            JsonNode payload = point.path("payload");
            String text = payload.path("text").asText("");

            if (!text.isBlank()) {
                chunks.add(text);
            }
        }

        return chunks;
    }
}

