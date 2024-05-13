package capstone.capstone2024.domain.app.dto.request;

import capstone.capstone2024.domain.app.domain.App;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class AppUsageCreateRequestDto {
    private String appName;
    private String appPackageName;
    private Integer usageTime;
    private String appUrl;

    public App toEntity() {
        return App.builder()
                .appName(this.appName)
                .usageTime(this.usageTime)
                .appPackageName(this.appPackageName)
                .appUrl(this.appUrl)
                .build();
    }
}
