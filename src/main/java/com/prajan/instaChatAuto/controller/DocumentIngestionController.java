package com.prajan.instaChatAuto.controller;

import com.prajan.instaChatAuto.service.QdrantIngestionService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentIngestionController {

    private final QdrantIngestionService ingestionService;

    public DocumentIngestionController(
            QdrantIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/ingest")
    public Map<String, Object> ingest() throws IOException {
        int count = ingestionService.ingestMeowMart();

        return Map.of(
                "status", "success",
                "message", "MeowMart document indexed",
                "chunksStored", count
        );
    }
}

