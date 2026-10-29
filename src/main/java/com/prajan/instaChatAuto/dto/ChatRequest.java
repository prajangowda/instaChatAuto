package com.prajan.instaChatAuto.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest{
    String query;
    String conversationId;
    String message;
}
