package capstone.capstone2024.domain.app.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AppResponseDto {
    private String appName;
    private Integer usageTime;
}
