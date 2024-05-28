package capstone.capstone2024.domain.photo.application;

import capstone.capstone2024.domain.photo.domain.Photo;
import capstone.capstone2024.domain.photo.domain.PhotoRepository;
import capstone.capstone2024.domain.photo.dto.request.PhotoCreateRequestDto;
import capstone.capstone2024.domain.photo.dto.response.PhotoResponseDto;
import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.user.domain.UserRepository;
import capstone.capstone2024.global.error.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static capstone.capstone2024.global.error.ErrorCode.ROW_DOES_NOT_EXIST;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveResult(PhotoCreateRequestDto photoCreateRequestDto, String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));

        Photo photo = user.getPhoto();

        if (photo != null) {
            photo.update(photoCreateRequestDto);
        } else {
            photo = Photo.builder()
                    .nature(photoCreateRequestDto.getNature())
                    .person(photoCreateRequestDto.getPerson())
                    .animal(photoCreateRequestDto.getAnimal())
                    .vehicle(photoCreateRequestDto.getVehicle())
                    .homeAppliance(photoCreateRequestDto.getHomeAppliance())
                    .food(photoCreateRequestDto.getFood())
                    .furniture(photoCreateRequestDto.getFurniture())
                    .daily(photoCreateRequestDto.getDaily())
                    .user(user)
                    .build();

            photoRepository.save(photo);
        }
    }

    @Transactional(readOnly = true)
    public PhotoResponseDto getResult(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));

        Photo photo = photoRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "사용자의 사진 분석 결과가 존재하지 않습니다."));


        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("nature", photo.getNature());
        resultMap.put("person", photo.getPerson());
        resultMap.put("animal", photo.getAnimal());
        resultMap.put("vehicle", photo.getVehicle());
        resultMap.put("homeAppliance", photo.getHomeAppliance());
        resultMap.put("food", photo.getFood());
        resultMap.put("furniture", photo.getFurniture());
        resultMap.put("daily", photo.getDaily());

        // 내림차순으로 정렬하여 카테고리 이름만 추출
        List<String> sortedCategories = resultMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());


        return PhotoResponseDto.builder()
                .sortedCategories(sortedCategories)
                .build();
    }
}
