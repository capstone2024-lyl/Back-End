package capstone.capstone2024.domain.user.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLogInRequestDto {
    private String loginId;
    private String password;
}
