package com.prajan.instaChatAuto.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import jakarta.annotation.Resource;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiService {


    private final ChatClient chatClient;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessage;

    public String generateReply(
            String conversationId,
            String message) {

        log.info("Generating AI reply for conversation {}", conversationId);

        return chatClient.prompt()
                .system(system ->
                        system.text(String.valueOf(systemMessage)))
                .user(message)
                .advisors(a -> a.param(
                        ChatMemory.CONVERSATION_ID,
                        conversationId
                ))
                .call()
                .content();
    }

    public Flux<String> generateReplyStream(
            String conversationId,
            String message) {

        log.info("Streaming AI reply for conversation {}", conversationId);

        return chatClient.prompt()
                .system(system->system.text(String.valueOf(systemMessage)))
                .user(message)
                .advisors(a -> a.param(
                        ChatMemory.CONVERSATION_ID,
                        conversationId
                ))
                .stream()
                .content();
    }
}