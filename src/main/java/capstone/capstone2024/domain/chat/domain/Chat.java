package capstone.capstone2024.domain.chat.domain;
import capstone.capstone2024.domain.base.BaseEntity;
import capstone.capstone2024.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Chat extends BaseEntity {

    @Column(name = "energy")
    private Integer energy;

    @Column(name = "recognition")
    private Integer recognition;

    @Column(name = "decision")
    private Integer decision;

    @Column(name = "lifeStyle")
    private Integer lifeStyle;

    @Enumerated(EnumType.STRING)
    @Column(name = "mbti")
    private MBTI mbti;

    @Column(name = "isChecked")
    private Boolean isChecked;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "chatCount")
    private Integer chatCount;

    public void update(
            Integer energy,
            Integer recognition,
            Integer decision,
            Integer lifeStyle,
            MBTI mbti,
            Boolean isChecked,
            Integer chatCount
    ){
        this.energy = energy;
        this.recognition = recognition;
        this.decision = decision;
        this.lifeStyle = lifeStyle;
        this.mbti = mbti;
        this.isChecked = isChecked;
        this.chatCount = chatCount;
    }

}
