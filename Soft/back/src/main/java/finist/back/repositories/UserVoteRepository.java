package finist.back.repositories;

import finist.back.model.UserVote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVoteRepository extends CrudRepository<UserVote, Long> {

    boolean existsByVoterIdAndBrideVoteId(Long voterId, Long brideVoteId);
}
