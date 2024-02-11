package finist.back.model.dto;

import lombok.Data;

@Data
public class NewCompetitionReqDTO {
    String name;
    String city;
    Long loveRequestId;
}
