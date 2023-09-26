package finist.back.model.enums.converters;

import finist.back.model.enums.UserRole;
import javax.persistence.*;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, String> {

    @Override
    public String convertToDatabaseColumn(UserRole userRole) {
        if (userRole == null) {
            return null;
        }
        return userRole.getCode();
    }

    @Override
    public UserRole convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(UserRole.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}