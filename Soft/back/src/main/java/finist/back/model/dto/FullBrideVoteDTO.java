package finist.back.model.dto;

import finist.back.model.BrideVote;
import finist.back.model.Candidate;
import finist.back.model.Task;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class FullBrideVoteDTO {

    Long id;
    Long competitionId;
    List<CandidateDTO> candidates = new ArrayList<>();
    LocalDate endTime;

    public FullBrideVoteDTO(BrideVote brideVote) {
        this.id = (brideVote.getId() != null) ? brideVote.getId() : null;
        this.competitionId = (brideVote.getCompetition() != null) ? brideVote.getCompetition().getId() : null;
        this.endTime = (brideVote.getEndTime() != null) ? brideVote.getEndTime() : null;

        if (!(brideVote.getCandidates() == null))
            this.candidates = brideVote.getCandidates().stream()
                    .map(CandidateDTO::new)
                    .collect(Collectors.toList());
    }
}
