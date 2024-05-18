package capstone.capstone2024.domain.user.domain;

import lombok.Getter;

@Getter
public enum UserNickname {
    //chat nickname
    HEAVY_TALKER("수다쟁이"),
    LAUGH_MASTER("웃음왕"),
    CURIOSITY_KING("호기심 왕"),

    //youtube category
    SHORTS_ADDICT("숏츠홀릭"),
    GAME_HOLIC("게임홀릭"),
    ATLETE_AT_HEART("마음만은 운동선수"),
    COUCH_DIRECTOR("방구석 영화 감독"),

    //photo
    NATURE_LOVER("자연인"),
    ANIMAL_LOVER("동물애호가"),
    NARCISSIST("사람이 좋아요"),

    //app usage
    SNS_ADDICT("SNS 중독"),
    ADDICT("휴대폰 중독"),
    WEBTOON_ADDICT("만화 주인공"),
    INTERNET_SURFER("인터넷 서퍼"),
    PHOTOGRAPHER("사진작가"),
    F1_DRIVER("F1 운전기사"),
    DOPAMINE_DETOXER("도파민 디톡서");

    private final String description;

    UserNickname(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
