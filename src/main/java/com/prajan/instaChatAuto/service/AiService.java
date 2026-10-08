package com.prajan.instaChatAuto.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateReply(String message) {
        log.info("Generating AI reply for message: {}", message);
        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }
}
