package capstone.capstone2024.domain.user.dto.request;

import capstone.capstone2024.domain.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import capstone.capstone2024.domain.user.domain.UserRole;

import java.time.LocalDate;

@Data
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

    private LocalDate birthday;


    private MultipartFile profileImage;


    public User toEntity(String encodedPassword, String imageUrl) {
        return User.builder()
                .loginId(this.loginId)
                .password(encodedPassword)
                .name(this.name)
                .birthday(this.birthday)
                .role(UserRole.USER)
                .profileImageUrl(imageUrl)
                .build();
    }
}
