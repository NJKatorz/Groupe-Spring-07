package be.vinci.ipl.projet2024.group07.targets.repositories;

import be.vinci.ipl.projet2024.group07.targets.models.Target;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TargetsRepository extends CrudRepository<Target, Integer> {

  @Query("select t from targets t where t.servers >= ?1 and t.servers <= ?2")
  Iterable<Target> findAllByServersBetween(int min, int max);
}
