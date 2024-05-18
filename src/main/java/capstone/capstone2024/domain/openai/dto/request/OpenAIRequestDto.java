package capstone.capstone2024.domain.openai.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class OpenAIRequestDto {

    private String model;
    private List<Map<String, String>> messages;
}