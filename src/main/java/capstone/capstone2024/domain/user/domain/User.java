package capstone.capstone2024.domain.user.domain;

import capstone.capstone2024.domain.app.domain.App;
import capstone.capstone2024.domain.base.BaseEntity;
import capstone.capstone2024.domain.chat.domain.Chat;
import capstone.capstone2024.domain.nickname.domain.Nickname;
import capstone.capstone2024.domain.photo.domain.Photo;
import capstone.capstone2024.domain.youtube.domain.YoutubeCategories;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(name = "loginId", unique = true, length = 30)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "birthday")
    private LocalDate birthday;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Chat mbti;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Photo photo;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<App> appList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<YoutubeCategories> youtubeCategories;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Nickname> nickname;

    @Column(name = "role")
    private UserRole role;

    @Column(name = "profileImageUrl", length = 500, nullable = true)
    private String profileImageUrl;

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

}
