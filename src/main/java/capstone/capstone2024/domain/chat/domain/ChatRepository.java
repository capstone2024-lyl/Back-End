package capstone.capstone2024.domain.chat.domain;

import capstone.capstone2024.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByUserId(Long userId);
}
