package capstone.capstone2024.domain.youtube.domain;

import capstone.capstone2024.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class YoutubeChannel extends BaseEntity {

    @Column(name = "channelName")
    private String channelName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private YoutubeCategory category;

}
