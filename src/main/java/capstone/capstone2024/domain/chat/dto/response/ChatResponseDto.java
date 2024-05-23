package capstone.capstone2024.domain.chat.dto.response;


import capstone.capstone2024.domain.chat.domain.MBTI;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatResponseDto {
    private Integer energy;
    private Integer recognition;
    private Integer decision;
    private Integer lifeStyle;
    private MBTI mbti;
    private Boolean isChecked;

}
