package capstone.capstone2024.domain.app.domain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Long> {
    List<App> findByUserIdOrderByUsageTimeDesc(Long userId);

    Optional<App> findById(Long appId);

    List<App> findByUserId(Long id);
}
