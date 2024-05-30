package capstone.capstone2024.domain.nickname.domain;

import lombok.Getter;

@Getter
public enum Nickname {
    //chat nickname
    HEAVY_TALKER("수다쟁이"),
    LAUGH_MASTER("웃음왕"),
    CURIOSITY_KING("호기심 왕"),

    //youtube category
    ENTERTAINMENT_PD("예능 국장"),
    GAME_HOLIC("게임홀릭"),
    ATLETE_AT_HEART("마음만은 운동선수"),
    COUCH_DIRECTOR("방구석 영화 감독"),
    KNOWLEDGE_SEEKER("지식 탐구자"),
    SCIENCE_GEEK("과학 덕후"),
    TRAVELER("마음만은 여행중"),
    COMEDY_LOVER("코미디가 좋아"),
    PET_LOVER("애완동물 애호가"),
    NEWS_JUNKIE("시사 박사"),
    TECH_MASTER("아이티 전문가"),

    //photo
    NATURE_LOVER("자연인"),
    ANIMAL_LOVER("동물애호가"),
    INSIDER("인싸"),
    CAR_ENTHUSIAST("자동차 애호가"),
    TECH_GEEK("가전제품 전문가"),
    FOODIE("미식가"),
    FURNITURE_CONNOISSEUR("가구 애호가"),
    DAILY_OBSERVER("일상 사진가"),

    //app usage
    SNS_ADDICT("SNS 중독"),
    ADDICT("휴대폰 중독"),
    WEBTOON_ADDICT("만화 주인공"),
    INTERNET_SURFER("인터넷 서퍼"),
    PHOTOGRAPHER("사진작가"),
    F1_DRIVER("F1 운전기사"),
    DOPAMINE_DETOXER("도파민 디톡서"),
    HEALTY_USER("건강한 사용량"),
    MUSIC_LOVER("나는 (듣기)가수다"),
    NEWS_HUNTER("시사왕"),
    VIDEO_ADDICT("글보단 영상이지"),
    STUDY_MASTER("공부가 좋아");


    private final String description;

    Nickname(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
