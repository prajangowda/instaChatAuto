package com.prajan.instaChatAuto.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prajan.instaChatAuto.service.AiService;
import com.prajan.instaChatAuto.service.InstagramMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/webhook")
public class InstagramWebhookController {

    @Value("${instagram.webhook.verify-token}")
    private String webhookVerifyToken;

    private final ObjectMapper objectMapper;
    private final InstagramMessageService messagingService;
    private final AiService aiService;

    // Keeps track of messages that have already been processed
    private final Set<String> processedMessageIds =
            ConcurrentHashMap.newKeySet();

    @GetMapping
    public String verifyWebhook(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.verify_token", required = false) String verifyToken,
            @RequestParam(name = "hub.challenge", required = false) String challenge) {

        if ("subscribe".equals(mode)
                && webhookVerifyToken.equals(verifyToken)) {

            return challenge;
        }

        return "Verification failed";
    }

    @PostMapping
    public String receiveMessage(@RequestBody String payload) {

        try {

            log.info("Instagram webhook received");

            JsonNode root = objectMapper.readTree(payload);

            JsonNode messaging = root
                    .path("entry")
                    .get(0)
                    .path("messaging")
                    .get(0);

            if (messaging.has("message")
                    && messaging.path("message").has("text")
                    && !messaging.path("message")
                    .path("is_echo")
                    .asBoolean(false)) {

                // Unique ID of this Instagram message
                String messageId = messaging
                        .path("message")
                        .path("mid")
                        .asText();

                // Check whether this message was already processed
                if (!processedMessageIds.add(messageId)) {

                    log.info(
                            "Duplicate message ignored. Message ID: {}",
                            messageId
                    );

                    return "EVENT_RECEIVED";
                }

                String senderId = messaging
                        .path("sender")
                        .path("id")
                        .asText();

                String messageText = messaging
                        .path("message")
                        .path("text")
                        .asText();

                log.info("Sender: {}", senderId);
                log.info("Message: {}", messageText);
                log.info("Message ID: {}", messageId);

                // Generate AI response
                String aiReply = aiService.generateReply( senderId, messageText);

                log.info("AI Reply: {}", aiReply);

                // Send ONLY ONE reply
                messagingService.sendMessage(
                        senderId,
                        aiReply
                );

                log.info("Instagram reply sent");

            }

        } catch (Exception e) {

            log.error("Error processing webhook", e);
        }

        return "EVENT_RECEIVED";
    }
}