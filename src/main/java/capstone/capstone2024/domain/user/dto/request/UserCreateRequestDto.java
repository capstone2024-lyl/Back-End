package capstone.capstone2024.domain.user.dto.request;

import capstone.capstone2024.domain.user.domain.User;
import lombok.Builder;

public class UserCreateRequestDto {
    private String email;
    private String name;

    @Builder
    public UserCreateRequestDto(String email, String name){
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
