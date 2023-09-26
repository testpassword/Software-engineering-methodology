package finist.back.repositories;

import finist.back.model.Competition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends CrudRepository<Competition, Long> {
    List<Competition> findAll();
}
