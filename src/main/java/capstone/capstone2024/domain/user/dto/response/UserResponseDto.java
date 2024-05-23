package capstone.capstone2024.domain.user.dto.response;

import capstone.capstone2024.domain.app.dto.response.AppResponseDto;
import capstone.capstone2024.domain.user.domain.UserNickname;
import capstone.capstone2024.domain.youtube.domain.YoutubeChannel;
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

    private List<YoutubeChannel> category;

    private List<UserNickname> nicknames;

    private List<String> photo;

    private List<String> nickname;

}
