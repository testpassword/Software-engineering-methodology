package finist.back.model.dto;

import finist.back.model.Candidate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CandidateDTO {
    Long brideId;
    Integer points;


    public CandidateDTO(Candidate candidate) {
        this.brideId = (candidate.getBride() != null) ? candidate.getBride().getId() : null;
        this.points = (candidate.getPoints() != null) ? candidate.getPoints() : null;
    }
}
