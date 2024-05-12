package capstone.capstone2024.domain.user.dto.response;

import capstone.capstone2024.domain.app.domain.App;
import capstone.capstone2024.domain.category.domain.Category;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;


@Builder
@Data
public class UserResponseDto {
    private String loginId;

    private String name;

    private LocalDate birthday;

    private String mbti;

    private List<App> app;

    private List<Category> category;

    private List<String> photo;

    private List<String> nickname;

}
