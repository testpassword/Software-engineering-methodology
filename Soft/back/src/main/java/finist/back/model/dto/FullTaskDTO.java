package finist.back.model.dto;

import finist.back.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullTaskDTO {

    private Long id;
    private Long executorId;
    private String report;
    private String text;
    private Boolean completed;


    public FullTaskDTO(Task task) {
        this.id = task.getId();
        this.executorId = (task.getExecutor() != null) ? task.getExecutor().getId() : null;
        this.report = (task.getReport() != null) ? task.getReport() : null;
        this.report = (task.getText() != null) ? task.getText() : null;
        this.completed = task.getCompleted();
    }
}
