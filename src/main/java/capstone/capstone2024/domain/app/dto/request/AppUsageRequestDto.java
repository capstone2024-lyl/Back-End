package capstone.capstone2024.domain.app.dto.request;

import capstone.capstone2024.domain.app.domain.App;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AppUsageRequestDto {
    @NotBlank(message = "앱 사용기록이 비어있습니다.")
    String appUsage;
}
