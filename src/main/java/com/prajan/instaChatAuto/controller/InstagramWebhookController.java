package com.prajan.instaChatAuto.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prajan.instaChatAuto.service.AiService;
import com.prajan.instaChatAuto.service.InstagramMessageService;
import jdk.jfr.StackTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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

                String aiReply = aiService.generateReply(messageText);
                log.info("AI Reply: " + aiReply);
                messagingService.sendMessage(
                        senderId,
                        aiReply
                );

            }

        } catch (Exception e) {


            log.error("Error processing webhook:", e);
        }

        return "EVENT_RECEIVED";
    }


}