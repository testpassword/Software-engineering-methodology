package finist.back.model.dto;

import finist.back.model.Competition;
import finist.back.model.Task;
import finist.back.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullUserDTO {

    private List<Long> tasks;

    private List<Long> followsIds;

    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String phone;
    private String name;
    private LocalDate dateOfBirth;
    private String city;
    private String education;
    private String role;
    private String aboutSelf;
    private String aboutPartner;
    private Boolean isPairing;

    private String email;
    private Integer arrowsAmount;


    public FullUserDTO(User user) {
        this.id = user.getId();
        this.phone = user.getPhone();
        this.name = user.getName();
        this.dateOfBirth = user.getBirthDate();
        this.city = user.getCity();
        this.education = (user.getEducationType() != null) ? user.getEducationType().getCode() : null;
        this.role = (user.getRole() != null) ? user.getRole().getCode() : null;
        this.aboutSelf = user.getAboutSelf();
        this.aboutPartner = user.getAboutPartner();
        this.isPairing = user.getIsPairing();
        this.email = user.getEmail();
        this.arrowsAmount = user.getArrowsAmount();

        // Создаем список taskIds из id задач
        this.tasks = user.getTasks().stream()
                .map(Task::getId)
                .collect(Collectors.toList());

        this.followsIds = user.getFollowedCompetitions().stream()
                .map(Competition::getId)
                .collect(Collectors.toList());
    }
}
