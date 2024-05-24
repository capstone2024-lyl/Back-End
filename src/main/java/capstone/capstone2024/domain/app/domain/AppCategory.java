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
        packageToCategoryMap.put("com.google.android.youtube", VIDEO);
        packageToCategoryMap.put("com.sec.android.app.launcher", CHAT);
        packageToCategoryMap.put("com.sec.android.app.sbrowser", SNS);
        packageToCategoryMap.put("com.nhn.android.webtoon", WEBTOON);
        packageToCategoryMap.put("com.everytime.v2", SNS);
        packageToCategoryMap.put("us.zoom.videomeetings", CHAT);
        packageToCategoryMap.put("com.android.settings", LIFESTYLE);
        packageToCategoryMap.put("com.naver.clova.minute", CHAT);
        packageToCategoryMap.put("com.openai.chatgpt", CHAT);
        packageToCategoryMap.put("com.google.android.gms", CHAT);
        packageToCategoryMap.put("com.gramgames.tenten", GAME);
        packageToCategoryMap.put("com.google.android.apps.youtube.music", MUSIC);
        packageToCategoryMap.put("com.nhn.android.nmap", MAP);
        packageToCategoryMap.put("com.discord", CHAT);
        packageToCategoryMap.put("com.samsung.android.dialer", PHONE_DECORATION);
        packageToCategoryMap.put("com.sampleapp", OTHERS);
        packageToCategoryMap.put("com.samsung.android.messaging", PHONE_DECORATION);
        packageToCategoryMap.put("viva.republica.toss", LIFESTYLE);
        packageToCategoryMap.put("kr.co.symtra.cauid", LIFESTYLE);
        packageToCategoryMap.put("com.sec.android.app.myfiles", LIFESTYLE);
        packageToCategoryMap.put("com.samsung.android.spay", LIFESTYLE);
        packageToCategoryMap.put("notion.id", E_BOOK);
        packageToCategoryMap.put("teamDoppelGanger.SmarterSubway", MAP);
        packageToCategoryMap.put("com.sec.android.app.clockpackage", LIFESTYLE);
        packageToCategoryMap.put("com.google.android.documentsui", OTHERS);
        packageToCategoryMap.put("com.instructure.candroid.xinics.production", LIFESTYLE);
        packageToCategoryMap.put("com.sec.android.app.camera", LIFESTYLE);
        packageToCategoryMap.put("com.android.systemui", OTHERS);
        packageToCategoryMap.put("com.coupang.mobile", LIFESTYLE);
        packageToCategoryMap.put("com.android.vending", LIFESTYLE);
        packageToCategoryMap.put("com.samsung.android.forest", LIFESTYLE);
        packageToCategoryMap.put("com.example.untitled1", LIFESTYLE);



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
