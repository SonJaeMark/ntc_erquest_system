package com.github.sonjaemark.ntc_erquest_system.service.ai;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthLevel;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;

@Service
@Transactional
public class AIOpenRouterService extends AuthLevel implements IAiService{

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    protected AIOpenRouterService(AuthService authService) {
        super(authService);
    }

    @Override
    public String askAI(String userQuestion) {
        isAuthorized(List.of(UserRole.STUDENT));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 🔥 SYSTEM PROMPT (important for your project)
        String body = """
        {
        "model": "meta-llama/llama-3-8b-instruct:free",
        "messages": [
            {
            "role": "system",
            "content": "You are an assistant for NTC eRequest system. Help students with document requests."
            },
            {
            "role": "user",
            "content": "%s"
            }
        ]
        }
        """.formatted(userQuestion);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                request,
                Map.class
        );

        return extractContent(response.getBody());
    }

    // 🔥 Extract response from OpenRouter JSON
    private String extractContent(Map response) {
        try {
            var choices = (List<Map>) response.get("choices");
            var message = (Map) choices.get(0).get("message");
            return message.get("content").toString();
        } catch (Exception e) {
            return "Error processing AI response";
        }
    }

}
