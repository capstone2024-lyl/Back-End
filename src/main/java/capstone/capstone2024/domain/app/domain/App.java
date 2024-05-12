package capstone.capstone2024.domain.app.domain;

import capstone.capstone2024.domain.base.BaseEntity;
import capstone.capstone2024.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class App extends BaseEntity {


    @Column(name = "appName")
    private String appName;

    @Column(name = "usageTime", nullable = false)
    private Integer usageTime;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;

}
