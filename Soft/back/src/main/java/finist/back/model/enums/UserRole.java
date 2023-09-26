package finist.back.model.enums;

public enum UserRole {
    MATCHMAKER("matchmaker"), GROOM("groom"),
    BRIDE("bride"), GUEST("guest"),
    ASSISTANT("assistant"), ENEMY("enemy");
    private final String code;

    public static UserRole getByCode(String code) {
        for (UserRole userRole : values()) {
            if (userRole.getCode().equalsIgnoreCase(code)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Недопустимый код образования: " + code);
    }
    UserRole(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
