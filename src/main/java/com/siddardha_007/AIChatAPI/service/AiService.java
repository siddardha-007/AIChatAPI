package com.siddardha_007.AIChatAPI.service;

import com.siddardha_007.AIChatAPI.dto.GenerateRequest;
import com.siddardha_007.AIChatAPI.dto.TechnicalAnswer;
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
    private final String generatePrompt;

    public AiService(
            ChatClient.Builder chatClientBuilder,
            @Value("classpath:prompts/system-prompt.txt") Resource systemResource,
            @Value("classpath:prompts/generate-prompt.txt") Resource generateResource
            ) throws IOException {
        this.chatClient = chatClientBuilder.build();

        this.systemPrompt =
                systemResource.getContentAsString(StandardCharsets.UTF_8);

        this.generatePrompt =
                generateResource.getContentAsString(StandardCharsets.UTF_8);
    }

    public TechnicalAnswer generate(String prompt){

//        String userPrompt = generatePrompt
//                .replace("{topic}",request.getTopic())
//                .replace("{tone}",request.getTone())
//                .replace("{language}",request.getLanguage())
//                .replace("{request}",request.getRequest());

        return chatClient
                .prompt()
                .system(generatePrompt)
                .user(prompt)
                .call()
                .entity(
                        TechnicalAnswer.class,
                        spec -> spec.useProviderStructuredOutput()
                );
    }
}