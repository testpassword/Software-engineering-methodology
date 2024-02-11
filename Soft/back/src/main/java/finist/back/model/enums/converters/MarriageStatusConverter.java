//package finist.back.model.enums.converters;
//
//import finist.back.model.enums.MarriageStatus;
//import finist.back.model.enums.UserRole;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//import java.util.stream.Stream;
//
//@Converter(autoApply = true)
//public class MarriageStatusConverter implements AttributeConverter<MarriageStatus, String> {
//
//    @Override
//    public String convertToDatabaseColumn(MarriageStatus marriageStatus) {
//        if (marriageStatus == null) {
//            return null;
//        }
//        return marriageStatus.getCode();
//    }
//
//    @Override
//    public MarriageStatus convertToEntityAttribute(String code) {
//        if (code == null) {
//            return null;
//        }
//
//        return Stream.of(MarriageStatus.values())
//                .filter(c -> c.getCode().equals(code))
//                .findFirst()
//                .orElseThrow(IllegalArgumentException::new);
//    }
//}