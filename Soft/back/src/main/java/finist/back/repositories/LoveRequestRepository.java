package finist.back.repositories;

import finist.back.model.LoveRequest;
import finist.back.model.Task;
import finist.back.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoveRequestRepository extends CrudRepository<LoveRequest, Long> {

    List<LoveRequest> findAll();

    List<LoveRequest> findAllByIsAssignedFalse();

}
