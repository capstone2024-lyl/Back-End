//package capstone.capstone2024.domain.chat.domain;
//import capstone.capstone2024.domain.base.BaseEntity;
//import capstone.capstone2024.domain.user.domain.User;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Builder
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//public class Chat extends BaseEntity {
//
//    @Column(name = "neur")
//    private Integer neur;
//
//
//    @Column(name = "extr")
//    private Integer extr;
//
//
//    @Column(name = "open")
//    private Integer open;
//
//
//    @Column(name = "agree")
//    private Integer agree;
//
//
//    @Column(name = "cons")
//    private Integer cons;
//
//
//    @JoinColumn(name = "user_id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user;
//}
