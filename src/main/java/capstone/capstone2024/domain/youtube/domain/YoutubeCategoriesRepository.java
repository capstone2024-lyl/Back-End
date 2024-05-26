package capstone.capstone2024.domain.youtube.domain;

import capstone.capstone2024.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YoutubeCategoriesRepository extends JpaRepository<YoutubeCategories, Long> {

    List<YoutubeCategories> findByUserIdAndIsDeletedFalse(Long userId);
}
