package finist.back.model;

import finist.back.model.enums.CompetitionStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@Table(name="competitions")
public class Competition {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @OneToOne (optional=true, mappedBy="competition")
    private Marriage marriage;

    @Column(name = "status")
    private CompetitionStatus status;

    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="creator_id")
    private User creator; // создатель соревнования

    @OneToMany(mappedBy="parentCompetition", fetch= FetchType.LAZY)
    private Collection<Task> tasks;

    @ManyToMany(mappedBy = "followedCompetitions")
    Collection<User> followers;

    @OneToOne (mappedBy="competition")
    private BrideVote brideVote;

    public Competition(User creator, String name, String city){
        this.creator = creator;
        this.name = name;
        this.city = city;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

