package finist.back.model.enums.converters;

import finist.back.model.enums.EducationType;

import javax.persistence.*;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class EducationTypeConverter implements AttributeConverter<EducationType, String> {

    @Override
    public String convertToDatabaseColumn(EducationType educationType) {
        if (educationType == null) {
            return null;
        }
        return educationType.getCode();
    }

    @Override
    public EducationType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(EducationType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}