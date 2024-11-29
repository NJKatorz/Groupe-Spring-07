package be.vinci.ipl.projet2024.group07.users;

import be.vinci.ipl.projet2024.group07.users.models.UnsafeCredentials;
import be.vinci.ipl.projet2024.group07.users.models.User;
import be.vinci.ipl.projet2024.group07.users.repositories.AuthenticationProxy;
import be.vinci.ipl.projet2024.group07.users.repositories.ExploitProxy;
import be.vinci.ipl.projet2024.group07.users.repositories.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsersService {

  private final UsersRepository repository;

  private final ExploitProxy exploitProxy;

  private final AuthenticationProxy authenticationProxy;

  public UsersService(UsersRepository repository, AuthenticationProxy authenticationProxy, ExploitProxy exploitProxy) {
    this.repository = repository;
    this.exploitProxy = exploitProxy;
    this.authenticationProxy = authenticationProxy;
  }


  public User createOne(UnsafeCredentials user) {
    if (repository.existsByEmail(user.getEmail())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
    }
    UnsafeCredentials creditential = new UnsafeCredentials();
    creditential.setEmail(user.getEmail());
    creditential.setPassword(user.getPassword());
    User savedUser = repository.save(user.toUser());
    authenticationProxy.register(creditential);
    return savedUser;
  }

  // pour les tests d'autentification
  public User createAdmin(UnsafeCredentials user) {
    if (repository.existsByEmail(user.getEmail())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
    }
    UnsafeCredentials creditential = new UnsafeCredentials();
    creditential.setEmail(user.getEmail());
    creditential.setPassword(user.getPassword());
    User savedUser = repository.save(user.toAdmin());
    authenticationProxy.register(creditential);
    return savedUser;
  }


  public User readOne(Integer id) {
    return repository.findById(id).orElse(null);
  }

  public User getByEmail(String email) {
    return repository.findByEmail(email).orElse(null);
  }

  public boolean updateUserName(Integer userId, String newName) {
    User user = repository.findById(userId).orElse(null);
    if (user == null) {
      return false;
    }
    user.setName(newName);
    repository.save(user);
    return true;
  }
  public boolean updateUserRole(int userId, String newRole) {
    User user = repository.findById(userId).orElse(null);
    if (user == null) {
      return false;
    }
    user.setRole(newRole);
    repository.save(user);
    return true;
  }


  public boolean deleteOne(int id) {
    if (!repository.existsById(id)) return false;

    User findUser = repository.findById(id).orElse(null);
    if (findUser==null) return false;
    exploitProxy.delete(id);
    authenticationProxy.delete(findUser.getEmail());
    return true;
  }
}

