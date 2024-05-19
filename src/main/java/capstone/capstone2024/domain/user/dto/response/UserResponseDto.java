package capstone.capstone2024.domain.user.dto.response;

import capstone.capstone2024.domain.app.dto.response.AppResponseDto;
import capstone.capstone2024.domain.youtube.domain.Youtube;
import lombok.Builder;
import lombok.Data;

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

    private List<Youtube> category;

    private List<String> photo;

    private List<String> nickname;

}
