package capstone.capstone2024.domain.youtube.domain;

import capstone.capstone2024.domain.base.BaseEntity;
import capstone.capstone2024.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Youtube extends BaseEntity {

    @Column(name = "channelName")
    private String channelName;

//    @Column(name = "")
//    private String category;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

}
