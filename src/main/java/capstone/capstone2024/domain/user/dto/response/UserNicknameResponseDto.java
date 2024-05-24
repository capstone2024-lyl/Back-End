package capstone.capstone2024.domain.user.dto.response;

import capstone.capstone2024.domain.nickname.domain.Nickname;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserNicknameResponseDto {
    private String loginId;
    private List<Nickname> nicknames;
}
