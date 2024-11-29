package be.vinci.ipl.projet2024.group07.users;

import be.vinci.ipl.projet2024.group07.users.models.User;
import be.vinci.ipl.projet2024.group07.users.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UsersController {

  private final UsersService service;

  public UsersController(UsersService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<User> createOne(@RequestBody UnsafeCredentials user) {
    if (user.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Données manquantes");
    if (user.isPasswordTooShort()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Mot de passe trop court");

    User createdUser = service.createOne(user);

    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);

  }

  @PatchMapping("/{userId}/name")
  public ResponseEntity<Void> updateUserName(@PathVariable Integer userId, @RequestBody String newName) {
    boolean updated = service.updateUserName(userId, newName);
    if (newName ==null || newName.isBlank() || newName.isEmpty() || newName.equals("")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Donnée manquante");
    if (!updated) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PatchMapping("/{userId}/role")
  public ResponseEntity<Void> updateUserRole(@PathVariable Integer userId, @RequestBody String newRole) {
    boolean updated = service.updateUserRole(userId, newRole);
    if (!updated) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }


  @GetMapping
  public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
    User user = service.getByEmail(email);
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
    }
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public User readOne(@PathVariable Integer id) {
    User user = service.readOne(id);

    if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return user;
  }

  @DeleteMapping("/{id}")
  public void deleteOne(@PathVariable int id) {
    boolean deleted = service.deleteOne(id);
    if (!deleted) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
  }

  // pour les tests d'autentification
  @PostMapping("/admin")
  public ResponseEntity<User> createAdmin(@RequestBody UnsafeCredentials user) {
    if (user.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Données manquantes");
    if (user.isPasswordTooShort()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Mot de passe trop court");

    User createdUser = service.createAdmin(user);

    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);

  }
}