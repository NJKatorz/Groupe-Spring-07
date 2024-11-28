package be.vinci.ipl.projet2024.group07.targets.repositories;

import be.vinci.ipl.projet2024.group07.targets.models.Target;
import org.springframework.data.repository.CrudRepository;

public interface TargetsRepository extends CrudRepository<Target, Integer> {

  Iterable<Target> findAllByServersBetween(int min, int max);
}
