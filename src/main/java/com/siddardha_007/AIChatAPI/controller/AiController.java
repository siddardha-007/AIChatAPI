package com.siddardha_007.AIChatAPI.controller;

import com.siddardha_007.AIChatAPI.dto.GenerateRequest;
import com.siddardha_007.AIChatAPI.dto.GenerateResponse;
import com.siddardha_007.AIChatAPI.dto.TechnicalAnswer;
import com.siddardha_007.AIChatAPI.service.AiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private AiService aiService;

    public AiController(AiService aiService){
        this.aiService = aiService;
    }

    @PostMapping("/generate")
    public TechnicalAnswer generate(@RequestBody GenerateRequest request){
//        String result = aiService.generate(request);

        return aiService.generate(request.getRequest());
    }
}
