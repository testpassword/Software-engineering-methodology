package finist.back.model;

import finist.back.model.dto.FullUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
public class LoveRequest {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_assigned")
    Boolean isAssigned;

    @ManyToOne (optional=false, cascade=CascadeType.PERSIST)
    @JoinColumn (name="groom_id")
    User groom;

    @OneToOne (mappedBy="loveRequest")
    Competition competition;

    @ManyToOne (cascade=CascadeType.PERSIST)
    @JoinColumn (name="matchmaker_id")
    User matchmaker;


    public LoveRequest(){
        this.isAssigned = false;
    }
    public LoveRequest withGroom(User groom) {
        this.groom = groom;
        return this;
    }

    public LoveRequest withCompetition(Competition competition) {
        this.competition = competition;
        return this;
    }

    public LoveRequest withMatchMaker(User matchMaker) {
        this.matchmaker = matchMaker;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Boolean getAssigned() {
        return isAssigned;
    }

    public User getGroom() {
        return groom;
    }

    public User getMatchmaker() {
        return matchmaker;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAssigned(Boolean assigned) {
        isAssigned = assigned;
    }

    public void setGroom(User groom) {
        this.groom = groom;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public void setMatchmaker(User matchmaker) {
        this.matchmaker = matchmaker;
    }

}
