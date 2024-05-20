package capstone.capstone2024.domain.youtube.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeSubscribeResponseDto {
    private String id;
    private String title;
    private String description;
    private String channelId;

}
