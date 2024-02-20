package finist.back.model;

import finist.back.model.enums.CompetitionStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@Table(name="competitions")
public class Competition {

    @Getter
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "report")
    private String report;

    @OneToOne (optional=true, mappedBy="competition")
    private Marriage marriage;

    @Column(name = "status")
    private CompetitionStatus status;

    @ManyToOne (optional=false, cascade=CascadeType.PERSIST)
    @JoinColumn (name="creator_id")
    private User creator; // создатель соревнования

    @OneToMany(mappedBy="parentCompetition", fetch= FetchType.LAZY)
    private Collection<Task> tasks;

    @ManyToMany(mappedBy = "followedCompetitions")
    Collection<User> followers;

    @OneToOne (mappedBy="competition")
    private BrideVote brideVote;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="love_request_id")
    private LoveRequest loveRequest;

    public Competition(User creator, String name, String city, LoveRequest loveRequest){
        this.creator = creator;
        this.name = name;
        this.city = city;
        this.status = CompetitionStatus.IN_PROGRESS;
        this.loveRequest = loveRequest;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Marriage getMarriage() {
        return marriage;
    }
}

