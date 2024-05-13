package capstone.capstone2024.domain.user.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class UserLogInRequestDto {
    @NotBlank(message = "id가 비어있습니다.")
    private String loginId;

    @NotBlank(message = "password가 비어있습니다.")
    private String password;
}
