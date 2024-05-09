package capstone.capstone2024.domain.user.domain;

import capstone.capstone2024.domain.user.dto.response.UserResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
//    UserResponseDto findByName(String name);
//
//    UserResponseDto findByEmail(String email);
//

    boolean existsByName(String username);
    boolean existsByLoginId(String loginId);
    Optional<User> findByLoginId(String loginId);
}
