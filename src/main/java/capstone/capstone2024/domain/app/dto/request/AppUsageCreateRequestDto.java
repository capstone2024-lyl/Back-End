package capstone.capstone2024.domain.app.dto.request;

import capstone.capstone2024.domain.app.domain.App;
import capstone.capstone2024.domain.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppUsageCreateRequestDto {

    @NotBlank(message = "앱 패키지명이 비어있습니다.")
    private String appPackageName;

    @NotNull(message = "앱 사용시간이 비어있습니다.")
    private Integer usageTime;

    public App toEntity(User user) {
        return App.builder()
                .usageTime(this.usageTime)
                .appPackageName(this.appPackageName)
                .user(user)
                .build();
    }
}
