package capstone.capstone2024.domain.app.domain;

import java.util.HashMap;
import java.util.Map;

public enum AppCategory {
    GAME("게임", 180),
    MUSIC("음악", 240),
    NEWS("뉴스", 20),
    VIDEO("동영상", 720),
    SNS("SNS", 960),
    WEBTOON("웹툰", 120),
    CHAT("채팅", 120),
    MAP("지도", 180),
    LIFESTYLE("생활정보", 10),
    PHONE_DECORATION("폰꾸미기", 10),
    OTHERS("기타", 10),
    E_BOOK("e-book", 120);

    private final String description;
    private final Integer averageUsage;
    private static final Map<String, AppCategory> packageToCategoryMap = new HashMap<>();

    static {
        packageToCategoryMap.put("com.kakao.talk", CHAT);
        packageToCategoryMap.put("com.android.mms", CHAT);
        packageToCategoryMap.put("com.instagram.android", SNS);
        packageToCategoryMap.put("com.facebook.katana", SNS);
        // 추가적인 패키지명과 카테고리 매핑도 여기에 추가할 수 있습니다.
    }

    AppCategory(String description, Integer averageUsage) {
        this.description = description;
        this.averageUsage = averageUsage;
    }

    public String getDescription() {
        return description;
    }

    public double getAverageUsage() {return averageUsage; }

    public static AppCategory fromPackageName(String packageName) {
        return packageToCategoryMap.getOrDefault(packageName, OTHERS);
    }


}
