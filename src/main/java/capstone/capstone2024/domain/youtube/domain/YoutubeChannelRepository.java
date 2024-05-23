package capstone.capstone2024.domain.youtube.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface YoutubeChannelRepository extends JpaRepository<YoutubeChannel, Long> {

    Optional<YoutubeChannel> findByChannelName(String channelName);
}
