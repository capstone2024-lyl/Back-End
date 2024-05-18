package capstone.capstone2024.domain.openai.application;

import capstone.capstone2024.domain.openai.dto.request.OpenAIRequestDto;
import capstone.capstone2024.domain.openai.dto.response.OpenAIResponseDto;
import capstone.capstone2024.global.error.ErrorCode;
import capstone.capstone2024.global.error.exceptions.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    @Value("${spring.openai.api.key}")
    private String openaiApiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String translateText(String text) {
        WebClient client = WebClient.builder()
                .baseUrl(OPENAI_URL)
                .defaultHeader("Authorization", "Bearer " + openaiApiKey)
                .build();

        OpenAIRequestDto request = OpenAIRequestDto.builder()
                .model("gpt-4o")
                .messages(List.of(Map.of("role", "user", "content", "Translate the following text to English: " + text)))
                .build();

        OpenAIResponseDto response = client.post()
                .body(Mono.just(request), OpenAIRequestDto.class)
                .retrieve()
                .bodyToMono(OpenAIResponseDto.class)
                .block();

        if (response == null || response.getChoices().isEmpty()) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER, "Failed to get response from OpenAI");
        }

        return response.getChoices().get(0).getMessage().get("content").trim();
    }
}
