package capstone.capstone2024.domain.photo.domain;


import capstone.capstone2024.domain.base.BaseEntity;
import capstone.capstone2024.domain.photo.dto.request.PhotoCreateRequestDto;
import capstone.capstone2024.domain.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Photo extends BaseEntity {
    @Builder.Default
    private Integer nature = 0;
    @Builder.Default
    private Integer person = 0;
    @Builder.Default
    private Integer animal = 0;
    @Builder.Default
    private Integer vehicle = 0;
    @Builder.Default
    private Integer homeAppliance = 0; // 가전제품
    @Builder.Default
    private Integer food = 0;
    @Builder.Default
    private Integer furniture = 0;
    @Builder.Default
    private Integer daily = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;


    // 업데이트 메서드 추가
    public void update(PhotoCreateRequestDto dto) {
        this.nature += dto.getNature();
        this.person += dto.getPerson();
        this.animal += dto.getAnimal();
        this.vehicle += dto.getVehicle();
        this.homeAppliance += dto.getHomeAppliance();
        this.food += dto.getFood();
        this.furniture += dto.getFurniture();
        this.daily += dto.getDaily();
    }
}
