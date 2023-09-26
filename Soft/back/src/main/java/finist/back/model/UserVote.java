package finist.back.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_votes")
public class UserVote {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="candidate_id")
    private Candidate candidate;

    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="voter_id")
    private User voter;

    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="bride_vote_id")
    private BrideVote brideVote;
}
