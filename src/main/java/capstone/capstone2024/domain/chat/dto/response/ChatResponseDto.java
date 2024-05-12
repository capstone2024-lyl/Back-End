package capstone.capstone2024.domain.chat.dto.response;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatResponseDto {
    private Integer energy;
    private Integer recognition;
    private Integer decision;
    private Integer lifeStyle;
    private Enum mbti;
    private Boolean isChecked;
}
