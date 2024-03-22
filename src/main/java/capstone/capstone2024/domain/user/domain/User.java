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

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "avg_neur")
    private String avg_neur;


    @Column(name = "avg_extr")
    private String avg_extr;

}
