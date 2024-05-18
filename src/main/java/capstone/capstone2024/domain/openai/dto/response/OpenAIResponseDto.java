package capstone.capstone2024.domain.openai.dto.response;


import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OpenAIResponseDto {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Map<String, String> message;
    }
}