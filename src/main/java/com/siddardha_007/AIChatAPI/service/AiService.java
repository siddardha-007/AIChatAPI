package com.siddardha_007.AIChatAPI.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class AiService {
    private final ChatClient chatClient;
    private final String systemPrompt;

    public AiService(
            ChatClient.Builder chatClientBuilder,
            @Value("classpath:prompts/system-prompt.txt") Resource resource
            ) throws IOException {
        this.chatClient = chatClientBuilder.build();
        this.systemPrompt = resource.getContentAsString(StandardCharsets.UTF_8);
    }

    public String generate(String prompt){
        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(prompt)
                .call()
                .content();
    }
}
