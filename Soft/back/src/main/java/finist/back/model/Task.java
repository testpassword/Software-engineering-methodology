package finist.back.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "report")
    private String report;

    @Column(name = "is_completed")
    private Boolean completed;

    @ManyToOne (optional=true, cascade=CascadeType.ALL)
    @JoinColumn (name="competition_id")
    private Competition parentCompetition;

    @JsonIgnore
    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="executor_id")
    private User executor; //исполнитель задания - у одного задания один испонитель, у исполнителя - много заданий

    public Task(String text, String report, Competition competition, User user) {
        this.text = text;
        this.report = report;
        this.parentCompetition = competition;
        this.executor = user;
        this.completed = false;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public Competition getParentCompetition() {
        return parentCompetition;
    }
}
