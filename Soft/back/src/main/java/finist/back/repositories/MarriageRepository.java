package finist.back.repositories;

import finist.back.model.Marriage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarriageRepository extends CrudRepository<Marriage, Long> {
}
