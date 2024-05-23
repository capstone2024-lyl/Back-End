package capstone.capstone2024.domain.youtube.domain;

import capstone.capstone2024.domain.base.BaseEntity;
import capstone.capstone2024.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class YoutubeSubscribe extends BaseEntity {

    @Column(name = "channelName")
    private String channelName;


    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private YoutubeCategory category;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

}
