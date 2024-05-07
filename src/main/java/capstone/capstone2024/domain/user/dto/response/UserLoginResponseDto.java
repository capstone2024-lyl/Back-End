package capstone.capstone2024.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponseDto {
    private String accessToken;
}
