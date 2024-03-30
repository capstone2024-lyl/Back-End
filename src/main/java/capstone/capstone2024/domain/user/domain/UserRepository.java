package capstone.capstone2024.domain.user.domain;

import capstone.capstone2024.domain.user.dto.response.UserResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
//    UserResponseDto findByName(String name);
//
//    UserResponseDto findByEmail(String email);
//

}
