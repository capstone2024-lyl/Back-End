package capstone.capstone2024.domain.user.dto.response;

import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.user.domain.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Builder
@Data
public class UserResponseDto {

    private String loginId;

    private String name;

}
