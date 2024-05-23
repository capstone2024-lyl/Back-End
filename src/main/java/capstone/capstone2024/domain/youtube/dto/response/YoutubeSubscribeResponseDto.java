package capstone.capstone2024.domain.youtube.dto.response;

import capstone.capstone2024.domain.youtube.domain.YoutubeCategory;
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
    private YoutubeCategory category;

}
