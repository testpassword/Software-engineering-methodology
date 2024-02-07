package finist.back.model.dto;

import finist.back.model.Competition;
import finist.back.model.Task;
import finist.back.model.User;
import finist.back.model.enums.EducationType;
import finist.back.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
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

    public FullUserDTO withTasks(List<Long> tasks) {
        this.tasks = tasks;
        return this;
    }

    public FullUserDTO withFollowsIds(List<Long> followsIds) {
        this.followsIds = followsIds;
        return this;
    }

    public FullUserDTO withId(Long id) {
        this.id = id;
        return this;
    }

    public FullUserDTO withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public FullUserDTO withName(String name) {
        this.name = name;
        return this;
    }

    public FullUserDTO withDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public FullUserDTO withCity(String city) {
        this.city = city;
        return this;
    }

    public FullUserDTO withEducation(EducationType education) {
        this.education = education.getCode();
        return this;
    }

    public FullUserDTO withRole(UserRole role) {
        this.role = role.getCode();
        return this;
    }

    public FullUserDTO withAboutSelf(String aboutSelf) {
        this.aboutSelf = aboutSelf;
        return this;
    }

    public FullUserDTO withAboutPartner(String aboutPartner) {
        this.aboutPartner = aboutPartner;
        return this;
    }

    public FullUserDTO withIsPairing(Boolean isPairing) {
        this.isPairing = isPairing;
        return this;
    }

    public FullUserDTO withEmail(String email) {
        this.email = email;
        return this;
    }

    public FullUserDTO withArrowsAmount(Integer arrowsAmount) {
        this.arrowsAmount = arrowsAmount;
        return this;
    }

    public FullUserDTO() {
        this.tasks = Collections.emptyList(); // Если список задач равен null, используем пустой список
        this.followsIds = Collections.emptyList(); // Если список задач равен null, используем пустой список
        this.arrowsAmount = 0;
    }
    public FullUserDTO(String email, Long id) {
        this.id = id;
        this.email = email;
        new FullUserDTO();
    }
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
        if (user.getTasks() != null) {
            // Создаем список taskIds из id задач
            this.tasks = user.getTasks().stream()
                    .map(Task::getId)
                    .collect(Collectors.toList());
        } else {
            this.tasks = Collections.emptyList(); // Если список задач равен null, используем пустой список
        }

        if (user.getFollowedCompetitions() != null) {
            this.followsIds = user.getFollowedCompetitions().stream()
                    .map(Competition::getId)
                    .collect(Collectors.toList());
        }
        else {
            this.followsIds = Collections.emptyList(); // Если список задач равен null, используем пустой список
        }

    }
}
