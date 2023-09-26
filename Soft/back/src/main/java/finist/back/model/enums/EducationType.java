package finist.back.model.enums;

public enum EducationType {
    PAROCHIAL("Церковно-приходское"), SECONDARY_GENERAL("Среднее общее"), SECONDARY_VOCATIONAL("Среднее профессиональное"), HIGHER("Высшее");

    private final String code;

    public static EducationType getByCode(String code) {
        for (EducationType educationType : values()) {
            if (educationType.getCode().equalsIgnoreCase(code)) {
                return educationType;
            }
        }
        throw new IllegalArgumentException("Недопустимый код образования: " + code);
    }

    EducationType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
