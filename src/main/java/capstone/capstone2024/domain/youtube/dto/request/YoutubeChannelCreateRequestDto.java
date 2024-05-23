package capstone.capstone2024.domain.youtube.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeChannelCreateRequestDto {
    private String channelName;
}
