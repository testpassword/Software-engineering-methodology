package finist.back.model.dto;

import finist.back.model.LoveRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullLoveRequestDTO {

    Long id;
    Boolean isAssigned;

    Long groom_id;

    Long competition_id;

    Long matchmaker_id;


    public FullLoveRequestDTO(LoveRequest loveRequest) {
        if (loveRequest.getMatchmaker() != null){
            this.matchmaker_id = loveRequest.getMatchmaker().getId();
            this.isAssigned = true;
        }
        if(loveRequest.getCompetition() != null) {
            this.competition_id = loveRequest.getCompetition().getId();
        }
        if (loveRequest.getGroom() != null)
            this.groom_id = loveRequest.getGroom().getId();
        this.id = loveRequest.getId();
        this.isAssigned = loveRequest.getAssigned();
    }

    public Long getCompetition_id() {
        return competition_id;
    }

    public Long getMatchmaker_id() {
        return matchmaker_id;
    }
}
