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

    @Column(name = "mbti")
    private Enum mbti;

    @Column(name = "isChecked")
    private Boolean isCheckd;

}
