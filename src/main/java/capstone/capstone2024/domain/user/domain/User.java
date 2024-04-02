package capstone.capstone2024.domain.user.domain;

import capstone.capstone2024.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(name = "email", unique = true, length = 30)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "avg_neur")
    private Integer avg_neur;

    @Column(name = "avg_extr")
    private Integer avg_extr;

    @Column(name = "avg_open")
    private Integer avg_open;

    @Column(name = "avg_agree")
    private Integer avg_agree;

    @Column(name = "avg_cons")
    private Integer avg_cons;

}
