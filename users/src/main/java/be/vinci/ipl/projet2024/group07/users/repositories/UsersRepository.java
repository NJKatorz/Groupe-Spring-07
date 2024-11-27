package be.vinci.ipl.projet2024.group07.users.repositories;

import be.vinci.ipl.projet2024.group07.users.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersRepository extends CrudRepository<User, Integer> {
  boolean existsByEmail(String email);
  User findByEmail(String email);

}
