package capstone.capstone2024.domain.user.dto.request;

import capstone.capstone2024.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {
    private String email;
    private String name;

    @Builder
    public UserRequestDto(String email, String name){
        this.email = email;
        this.name = name;
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .name(name)
                .build();
    }
}
