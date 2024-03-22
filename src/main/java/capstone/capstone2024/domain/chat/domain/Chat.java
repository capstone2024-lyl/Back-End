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

    @Column(name = "neur")
    private String neur;


    @Column(name = "extr")
    private String extr;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
