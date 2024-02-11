//package finist.back.model.enums;
//
//public enum MarriageStatus {
//    NOT_STARTED("not_started"), IN_PROGRESS("in_progress"),
//    FINISHED_SUCCESS("finished_success"), FINISHED_UNSUCCESSFUL("finished");
//    private final String code;
//
//    public static MarriageStatus getByCode(String code) {
//        for (MarriageStatus userRole : values()) {
//            if (userRole.getCode().equalsIgnoreCase(code)) {
//                return userRole;
//            }
//        }
//        throw new IllegalArgumentException("Недопустимый статус: " + code);
//    }
//    MarriageStatus(String code) {
//        this.code = code;
//    }
//
//    public String getCode() {
//        return code;
//    }
//}
