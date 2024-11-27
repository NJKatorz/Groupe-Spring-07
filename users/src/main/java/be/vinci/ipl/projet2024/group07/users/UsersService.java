package be.vinci.ipl.projet2024.group07.users;

import be.vinci.ipl.projet2024.group07.users.models.User;
import be.vinci.ipl.projet2024.group07.users.models.UserWithCredentials;
import be.vinci.ipl.projet2024.group07.users.repositories.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

  private final UsersRepository repository;

  public UsersService(UsersRepository repository) {
    this.repository = repository;
  }


  public boolean createOne(UserWithCredentials user) {
    if (repository.existsByEmail(user.getEmail())) return false;

    repository.save(user.toUser());
    return true;
  }


  public User readOne(Integer id) {
    return repository.findById(id).orElse(null);
  }

  public boolean updateOne(UserWithCredentials user) {
    if (!repository.existsById(user.getId())) return false;

    repository.save(user.toUser());
    return true;
  }

  public boolean deleteOne(Integer id) {
    if (!repository.existsById(id)) return false;

    repository.deleteById(id);
    return true;
  }
}

