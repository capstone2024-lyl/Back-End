package capstone.capstone2024.domain.youtube.dto.response;


import capstone.capstone2024.domain.youtube.domain.YoutubeCategories;
import capstone.capstone2024.domain.youtube.domain.YoutubeCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeTop3CategoriesResponseDto {
    private List<YoutubeCategory> youtubeCategoryList;
    private Boolean isChecked;

}
