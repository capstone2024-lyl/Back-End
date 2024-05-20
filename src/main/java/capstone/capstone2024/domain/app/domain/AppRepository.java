package capstone.capstone2024.domain.app.domain;

import capstone.capstone2024.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Long> {
    List<App> findByUserIdOrderByUsageTimeDesc(Long userId);

    Optional<App> findById(Long appId);

}
