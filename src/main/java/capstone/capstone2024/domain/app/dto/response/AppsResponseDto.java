package capstone.capstone2024.domain.app.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AppsResponseDto {
    private List<AppResponseDto> apps;
    private Boolean isChecked;
}
