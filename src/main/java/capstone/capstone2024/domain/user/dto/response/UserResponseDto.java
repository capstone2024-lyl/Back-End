package capstone.capstone2024.domain.user.dto.response;

import capstone.capstone2024.domain.app.dto.response.AppsResponseDto;
import capstone.capstone2024.domain.chat.dto.response.ChatResponseDto;
import capstone.capstone2024.domain.nickname.domain.Nickname;
import capstone.capstone2024.domain.youtube.dto.response.YoutubeTop3CategoriesResponseDto;
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

    private ChatResponseDto mbti;

    private AppsResponseDto apps;

    private YoutubeTop3CategoriesResponseDto category;

    private List<String> nicknames;

    private List<String> photo;

    private String profileImageUrl;


}
