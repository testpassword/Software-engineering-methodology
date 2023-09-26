package finist.back.repositories;

import finist.back.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

    Optional<Task> findByParentCompetitionIdAndId(Long competitionId, Long taskId);
}
