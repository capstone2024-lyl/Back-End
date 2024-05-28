package capstone.capstone2024.domain.photo.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PhotoCreateRequestDto {

    private Integer nature;
    private Integer person;
    private Integer animal;
    private Integer vehicle;
    private Integer homeAppliance; // 가전제품
    private Integer food;
    private Integer furniture;
    private Integer daily;
}
