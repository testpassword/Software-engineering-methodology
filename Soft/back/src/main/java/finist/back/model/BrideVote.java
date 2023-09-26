package finist.back.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Data
@NoArgsConstructor
@Entity
@Table(name="bride_votes")
public class BrideVote {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "end_time")
    private LocalDate endTime; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @OneToOne(optional=false, cascade= CascadeType.ALL)
    @JoinColumn(name="competition_id")
    private Competition competition;

    @OneToMany(mappedBy="brideVote", fetch= FetchType.LAZY)
    private Collection<Candidate> candidates;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

//    public void initCandidates(List<User> membersByCompetition) {
//        this.candidates =  membersByCompetition;
//    }
}

