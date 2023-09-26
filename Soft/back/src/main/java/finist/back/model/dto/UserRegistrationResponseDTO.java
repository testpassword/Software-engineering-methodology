package finist.back.model.dto;

import finist.back.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationResponseDTO {
    private Long id;
    private String phone;
    private String email;

    public UserRegistrationResponseDTO(User user){
        this.id = user.getId();
        this.phone = user.getPhone();
        this.email = user.getEmail();
    }
}
