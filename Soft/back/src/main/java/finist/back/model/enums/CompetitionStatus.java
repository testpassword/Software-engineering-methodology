package finist.back.model.enums;

public enum CompetitionStatus {

    CREATED("CREATED"), IN_PROGRESS("IN_PROGRESS"), VOTING("VOTING"), WAITING_AGREEMENT("WAITING_AGREEMENT"), MARRIAGE("MARRIAGE");

    private final String code;

    CompetitionStatus(String code) {
        this.code = code;
    }

    public static CompetitionStatus getByCode(String code) {
        for (CompetitionStatus competitionStatus : values()) {
            if (competitionStatus.getCode().equalsIgnoreCase(code)) {
                return competitionStatus;
            }
        }
        throw new IllegalArgumentException("Недопустимый код статуса состязания: " + code);
    }

    public String getCode() {
        return code;
    }
}
