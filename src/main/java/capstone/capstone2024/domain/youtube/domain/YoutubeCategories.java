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
public class YoutubeCategories extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Enumerated(EnumType.STRING)
    private YoutubeCategory category;

    private Long categoryCount;

    private Boolean isDeleted;

    public YoutubeCategories updateIsDeleted(Boolean isDeleted){
        return YoutubeCategories.builder()
                .user(this.user)
                .category(this.category)
                .categoryCount(this.categoryCount)
                .isDeleted(isDeleted)
                .build();
    }
}
