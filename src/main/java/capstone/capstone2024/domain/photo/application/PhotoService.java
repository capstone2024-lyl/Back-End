package capstone.capstone2024.domain.photo.application;

import capstone.capstone2024.domain.nickname.application.NicknameService;
import capstone.capstone2024.domain.nickname.domain.Nickname;
import capstone.capstone2024.domain.photo.domain.Photo;
import capstone.capstone2024.domain.photo.domain.PhotoCategory;
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
    private final NicknameService nicknameService;

    @Transactional
    public void saveResult(PhotoCreateRequestDto photoCreateRequestDto, String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));

        Photo photo = photoRepository.findByUserId(user.getId()).orElse(null);

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
                .orElse(null);


        if(photo != null){
            Map<PhotoCategory, Integer> resultMap = new EnumMap<>(PhotoCategory.class);
            resultMap.put(PhotoCategory.NATURE, photo.getNature());
            resultMap.put(PhotoCategory.PERSON, photo.getPerson());
            resultMap.put(PhotoCategory.ANIMAL, photo.getAnimal());
            resultMap.put(PhotoCategory.VEHICLE, photo.getVehicle());
            resultMap.put(PhotoCategory.HOME_APPLIANCE, photo.getHomeAppliance());
            resultMap.put(PhotoCategory.FOOD, photo.getFood());
            resultMap.put(PhotoCategory.FURNITURE, photo.getFurniture());
            resultMap.put(PhotoCategory.DAILY, photo.getDaily());
            // 내림차순으로 정렬
            List<Map.Entry<PhotoCategory, Integer>> sortedEntries = resultMap.entrySet().stream()
                    .sorted(Map.Entry.<PhotoCategory, Integer>comparingByValue().reversed())
                    .collect(Collectors.toList());

            List<String> sortedCategories = sortedEntries.stream()
                    .map(entry -> entry.getKey().getValue())
                    .collect(Collectors.toList());

            List<Integer> categoryCounts = sortedEntries.stream()
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

            Nickname nickname = assignNicknameByPhoto(sortedEntries.get(0).getKey());
            nicknameService.addNickname(user.getLoginId(), nickname);

            return PhotoResponseDto.builder()
                    .sortedCategories(sortedCategories)
                    .categoryCounts(categoryCounts)
                    .isChecked(true)
                    .build();
        }

        return PhotoResponseDto.builder()
                .isChecked(false)
                .build();
    }



    private Nickname assignNicknameByPhoto(PhotoCategory category){
        return switch (category) {
            case NATURE -> Nickname.NATURE_LOVER;
            case PERSON -> Nickname.INSIDER;
            case ANIMAL -> Nickname.ANIMAL_LOVER;
            case VEHICLE -> Nickname.CAR_ENTHUSIAST;
            case HOME_APPLIANCE -> Nickname.TECH_GEEK;
            case FOOD -> Nickname.FOODIE;
            case FURNITURE -> Nickname.FURNITURE_CONNOISSEUR;
            case DAILY -> Nickname.DAILY_OBSERVER;
        };
    }
}
