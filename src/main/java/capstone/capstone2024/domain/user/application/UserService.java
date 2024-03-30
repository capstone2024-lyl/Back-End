package capstone.capstone2024.domain.user.application;

import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.user.domain.UserRepository;
import capstone.capstone2024.domain.user.dto.request.UserCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User create(UserCreateRequestDto userCreateRequestDto){
        return userRepository.save(userCreateRequestDto.toEntity());
    }

}
