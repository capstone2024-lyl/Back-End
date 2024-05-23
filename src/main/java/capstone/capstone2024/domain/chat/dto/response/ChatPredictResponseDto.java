package capstone.capstone2024.domain.chat.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatPredictResponseDto {
    private Integer energy;
    private Integer recognition;
    private Integer decision;
    private Integer lifeStyle;
    private Integer chatCount;

}
