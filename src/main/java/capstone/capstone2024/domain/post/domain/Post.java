package capstone.capstone2024.domain.post.domain;

import capstone.capstone2024.domain.base.BaseEntity;
import capstone.capstone2024.domain.board.domain.Board;
import capstone.capstone2024.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity {


    @Column(columnDefinition = "TEXT", name = "content", nullable = false)
    private String content;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}
