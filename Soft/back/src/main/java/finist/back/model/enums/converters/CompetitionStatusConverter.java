package finist.back.model.enums.converters;

import finist.back.model.enums.CompetitionStatus;

import javax.persistence.*;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class CompetitionStatusConverter  implements AttributeConverter<CompetitionStatus, String> {

    @Override
    public String convertToDatabaseColumn(CompetitionStatus competitionStatus) {
        if (competitionStatus == null) {
            return null;
        }
        return competitionStatus.getCode();
    }

    @Override
    public CompetitionStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(CompetitionStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}