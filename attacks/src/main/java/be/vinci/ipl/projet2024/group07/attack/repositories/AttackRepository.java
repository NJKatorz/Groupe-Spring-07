package be.vinci.ipl.projet2024.group07.attack.repositories;

import be.vinci.ipl.projet2024.group07.attack.models.Attack;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttackRepository extends CrudRepository<Attack, Integer> {

}
