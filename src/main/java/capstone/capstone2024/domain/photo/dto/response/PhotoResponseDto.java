package capstone.capstone2024.domain.photo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PhotoResponseDto {
    private List<String> sortedCategories;
    private List<Integer> categoryCounts;
    private Boolean isChecked;
}