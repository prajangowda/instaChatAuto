package com.prajan.instaChatAuto.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DocumentIngestionService {

    private final VectorStore vectorStore;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public int ingestMeowMartDocument() throws IOException {

        ClassPathResource resource =
                new ClassPathResource("company-docs/meowmart-products.txt");

        String content = new String(
                resource.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        // Split at Markdown section headings.
        String[] sections = content.split("(?m)(?=^## )");

        List<Document> documents = new ArrayList<>();

        for (int i = 0; i < sections.length; i++) {
            String chunk = sections[i].trim();

            if (chunk.isBlank()) {
                continue;
            }

            Document document = new Document(
                    chunk,
                    Map.of(
                            "source", "meowmart-products.txt",
                            "company", "MeowMart",
                            "chunkIndex", i
                    )
            );

            documents.add(document);
        }

        // Spring AI generates embeddings and saves them
        // through the configured MariaDB VectorStore.
        vectorStore.add(documents);

        return documents.size();
    }
}

