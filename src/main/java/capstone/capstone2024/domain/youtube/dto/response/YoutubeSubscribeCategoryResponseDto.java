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
public class YoutubeSubscribeCategoryResponseDto {
    private YoutubeCategory category;
}
