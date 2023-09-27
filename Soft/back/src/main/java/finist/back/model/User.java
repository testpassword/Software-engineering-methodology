package finist.back.model;

import finist.back.model.enums.EducationType;
import finist.back.model.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table (name="users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "aboutSelf")
    private String aboutSelf;

    @Column(name = "aboutPartner")
    private String aboutPartner;

    @Column(name = "role")
    private UserRole role;

    @Column(name = "education")
    private EducationType educationType;

    @Column(name = "isPairing")
    private Boolean isPairing;

    @Column(name = "arrowsAmount")
    private Integer arrowsAmount = 0;

    @Column(name = "dateOfBirth")
    LocalDate birthDate;

    @OneToMany (mappedBy="creator", fetch=FetchType.LAZY)
    private Collection<Competition> createdCompetitions; // соревнования, созданные пользователем

    @OneToMany(mappedBy="executor", fetch= FetchType.LAZY)
    private Collection<Task> tasks; // задания, назначенные на пользователя | //исполнитель задания - у одного задания один испонитель, у исполнителя - много заданий

    @ManyToMany
    @JoinTable(
            name = "follows",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "competition_id"))
    Collection<Competition> followedCompetitions;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_to_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User(String email, String password, String phone, Role role) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.roles.add(role);
    }
}
