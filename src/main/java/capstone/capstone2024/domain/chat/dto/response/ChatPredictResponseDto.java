package capstone.capstone2024.domain.chat.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatPredictResponseDto {
    @JsonProperty(value = "decision")
    private Integer decision;

    @JsonProperty(value = "energy")
    private Integer energy;

    @JsonProperty(value = "lifeStyle")
    private Integer lifeStyle;

    @JsonProperty(value = "recognition")
    private Integer recognition;
}
