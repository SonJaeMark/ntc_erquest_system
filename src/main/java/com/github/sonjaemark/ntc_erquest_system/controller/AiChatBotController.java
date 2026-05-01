package com.github.sonjaemark.ntc_erquest_system.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.sonjaemark.ntc_erquest_system.service.ai.IAiService;

@RestController
@RequestMapping("/api/ai")
public class AiChatBotController {

    private final IAiService aiService;

    public AiChatBotController(IAiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody Map<String, String> request) {

        String question = request.get("question");
        String response = aiService.askAI(question);

        return ResponseEntity.ok(response);
    }
}
