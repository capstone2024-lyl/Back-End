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

    @Column(name = "appPackageName")
    private String appPackageName;

    @Column(name = "usageTime", nullable = false)
    private Integer usageTime;

    @Column(name = "appUrl")
    private String appUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

}
