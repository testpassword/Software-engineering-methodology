package finist.back.repositories;

import finist.back.model.BrideVote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrideVoteRepository extends CrudRepository<BrideVote, Long> {
}
