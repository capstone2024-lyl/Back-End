package capstone.capstone2024.domain.user.dto.request;

import capstone.capstone2024.domain.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import capstone.capstone2024.domain.user.domain.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateRequestDto {

    @NotBlank(message = "id가 비어있습니다.")
    private String loginId;

    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String password;

    @NotBlank(message = "비밀번호를 재입력하세요")
    private String passwordCheck;

    @NotBlank(message = "닉네임이 비어있습니다.")
    private String name;

    @NotBlank(message = "생년월일이 비어있습니다.")
    private LocalDate birthday;



    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .name(this.name)
                .role(UserRole.USER)
                .birthday(this.birthday)
                .build();
    }
    public User toEntity(String encodedPassword) {
        return User.builder()
                .loginId(this.loginId)
                .password(encodedPassword)
                .name(this.name)
                .role(UserRole.USER)
                .birthday(this.birthday)
                .build();
    }
}
