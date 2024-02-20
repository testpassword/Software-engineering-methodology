package finist.back.model.dto;

import finist.back.model.Competition;
import finist.back.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullCompetitionDTO {

    private Long id;
    private String name;
    private String city;
    private String report;
    private String status;
    private Collection<Long> tasksIds = new ArrayList<>();
//    Collection<User> followers;
    private Long loveRequestId;
    private Integer followersAmount = 0;


    public FullCompetitionDTO(Competition competition) {
      this.id = competition.getId();
      this.name = competition.getName();
      this.city = competition.getCity();
      this.report = competition.getReport();
      this.status = (competition.getStatus() != null) ? competition.getStatus().getCode() : null;
      this.loveRequestId = (competition.getLoveRequest() != null) ? competition.getLoveRequest().getId() : null;

      if (!(competition.getTasks() == null))
          this.tasksIds = competition.getTasks().stream()
                  .map(Task::getId)
                  .collect(Collectors.toList());

      if (competition.getFollowers() != null)
          this.followersAmount = competition.getFollowers().size();
    }
}
