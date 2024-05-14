package capstone.capstone2024.domain.user.dto.response;

import capstone.capstone2024.domain.app.domain.App;
import capstone.capstone2024.domain.app.dto.response.AppResponseDto;
import capstone.capstone2024.domain.category.domain.Category;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;


@Builder
@Data
public class UserResponseDto {
    private String loginId;

    private String name;

    private LocalDate birthday;

    private String mbti;

    private List<AppResponseDto> apps;

    private List<Category> category;

    private List<String> photo;

    private List<String> nickname;

}
