package finist.back.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "points")
    Integer points = 0;

    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="bride_id")
    private User bride;

    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="bride_vote_id")
    private BrideVote brideVote;

    @OneToMany(mappedBy="candidate", fetch= FetchType.LAZY)
    private Collection<UserVote> userVotes;

    public void addPoint(Integer pointsAmount){
        this.points += pointsAmount;
    }
}
