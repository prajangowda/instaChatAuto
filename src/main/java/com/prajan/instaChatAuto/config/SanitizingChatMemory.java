package com.prajan.instaChatAuto.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;

import java.util.List;
import java.util.Map;

public class SanitizingChatMemory implements ChatMemory {

    private final ChatMemory delegate;

    public SanitizingChatMemory(ChatMemory delegate) {
        this.delegate = delegate;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        delegate.add(conversationId, messages.stream().map(this::sanitize).toList());
    }

    @Override
    public List<Message> get(String conversationId) {
        return delegate.get(conversationId).stream().map(this::sanitize).toList();
    }

    @Override
    public void clear(String conversationId) {
        delegate.clear(conversationId);
    }

    // Strip metadata (reasoning etc.), keep text + tool calls
    private Message sanitize(Message m) {
        if (m instanceof AssistantMessage am) {
            return AssistantMessage.builder()
                    .content(am.getText())
                    .properties(Map.of())          // drops reasoning/metadata
                    .toolCalls(am.getToolCalls())  // keeps tool calls
                    .build();
        }
        return m;
    }
}