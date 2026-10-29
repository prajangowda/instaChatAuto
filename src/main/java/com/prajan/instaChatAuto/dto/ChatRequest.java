package com.prajan.instaChatAuto.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class ChatRequest{
    String query;
    String conversationId;
    String message;
}
