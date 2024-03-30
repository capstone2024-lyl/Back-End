package capstone.capstone2024.domain.user.dto.response;

import capstone.capstone2024.domain.user.domain.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final Long userId;
    private final String email;
    private final String name;

    public UserResponseDto(User user){
        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
    }
}
