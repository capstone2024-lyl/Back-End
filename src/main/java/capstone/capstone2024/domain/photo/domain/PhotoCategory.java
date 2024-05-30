package capstone.capstone2024.domain.photo.domain;

public enum PhotoCategory {
    NATURE("자연"),
    PERSON("인물"),
    ANIMAL("동물"),
    VEHICLE("교통 수단"),
    HOME_APPLIANCE("가전제품"),
    FOOD("음식"),
    FURNITURE("가구"),
    DAILY("일상");

    private final String value;

    PhotoCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

