package capstone.capstone2024.domain.app.domain;

import java.util.HashMap;
import java.util.Map;

public enum AppCategory {
    GAME("게임"),
    MUSIC("음악"),
    NEWS("뉴스"),
    VIDEO("동영상"),
    SNS("SNS"),
    WEBTOON("웹툰"),
    CHAT("채팅"),
    MAP("지도"),
    LIFESTYLE("생활정보"),
    PHONE_DECORATION("폰꾸미기"),
    OTHERS("기타"),
    E_BOOK("e-book");

    private final String description;
    private static final Map<String, AppCategory> packageToCategoryMap = new HashMap<>();

    static {
        packageToCategoryMap.put("com.kakao.talk", CHAT);
        packageToCategoryMap.put("com.android.mms", CHAT);
        packageToCategoryMap.put("com.instagram.android", SNS);
        packageToCategoryMap.put("com.facebook.katana", SNS);
        // 추가적인 패키지명과 카테고리 매핑도 여기에 추가할 수 있습니다.
    }

    AppCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static AppCategory fromPackageName(String packageName) {
        return packageToCategoryMap.getOrDefault(packageName, OTHERS);
    }
}
