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

    @Column(name = "appPackageName", nullable = false)
    private String appPackageName;

    @Column(name = "usageTime", nullable = false)
    private Integer usageTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "appCategory")
    private AppCategory appCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public void updateUsageTime(Integer newUsageTime) {
        if (newUsageTime != null && newUsageTime >= 0) {
            this.usageTime = newUsageTime;
        }
    }

}
