package com.prajan.instaChatAuto.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prajan.instaChatAuto.service.InstagramMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class InstagramWebhookController {

    @Value("${instagram.webhook.verify-token}")
    private String webhookVerifyToken;

    private final ObjectMapper objectMapper;
    private final InstagramMessageService messagingService;

    public InstagramWebhookController(
            ObjectMapper objectMapper,
            InstagramMessageService messagingService) {

        this.objectMapper = objectMapper;
        this.messagingService = messagingService;
    }

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

            System.out.println("Instagram webhook received:");
            System.out.println(payload);

            JsonNode root = objectMapper.readTree(payload);

            JsonNode messaging = root
                    .path("entry")
                    .get(0)
                    .path("messaging")
                    .get(0);

            if (messaging.has("message")
                    && messaging.path("message").has("text")
                    && !messaging.path("message").path("is_echo").asBoolean(false)) {

                String senderId = messaging
                        .path("sender")
                        .path("id")
                        .asText();

                String messageText = messaging
                        .path("message")
                        .path("text")
                        .asText();

                System.out.println("Sender: " + senderId);
                System.out.println("Message: " + messageText);

                messagingService.sendMessage(
                        senderId,
                        "Hello! 👋 You said: " + messageText
                );

            }

        } catch (Exception e) {

            System.err.println("Error processing webhook:");
            e.printStackTrace();
        }

        return "EVENT_RECEIVED";
    }
}