package capstone.capstone2024.domain.youtube.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeSubscribeResponseDto {
    private String channelName;
//    private String description;
//    private String channelId;

}
