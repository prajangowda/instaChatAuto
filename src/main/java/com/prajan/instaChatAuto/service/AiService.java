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

        String context = """
        You are assisting users through Instagram.

        Application:
        InstaChatBot

        Purpose:
        Automatically reply to Instagram messages,for Business TrueHunt which is Cat wet food startup.

        Current capabilities:
        - Answer general questions
        - Have natural conversations
        - Keep replies concise
        """;

        return chatClient
                .prompt()
                .system("""
                        You are InstaChatBot, an AI assistant that replies to
                        Instagram messages for Business TrueHunt which is Cat wet food startup.

                        Your behavior:
                        - Be friendly, natural, and conversational.
                        - Keep replies concise and suitable for Instagram.
                        - Answer the user's question directly.
                        - Use simple language.
                        - Do not invent facts or information.
                        - If you do not have enough information, say that you
                          don't know instead of making something up.
                        """)
                .user("""
                Context:
                %s

                User message:
                %s
                """.formatted(context, message))
                .call()
                .content();
    }
}