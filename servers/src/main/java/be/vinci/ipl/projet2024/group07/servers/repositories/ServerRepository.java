package be.vinci.ipl.projet2024.group07.servers.repositories;

import be.vinci.ipl.projet2024.group07.servers.models.Server;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepository extends CrudRepository<Server, Integer> {

  Iterable<Server> findByTargetId(int targetId);

  @Transactional
  void deleteByTargetId(int targetId);
}
