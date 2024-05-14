package capstone.capstone2024.domain.app.dto.response;


import capstone.capstone2024.domain.app.domain.App;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AppResponseDto {
    private String appPackageName;
    private Integer usageTime;

}
