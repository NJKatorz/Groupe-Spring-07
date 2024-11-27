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
  public ResponseEntity<Void> createOne(@RequestBody UserWithCredentials user) {
    if (user.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    boolean created = service.createOne(user);

    if (!created) throw new ResponseStatusException(HttpStatus.CONFLICT);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public User readOne(@PathVariable Integer id) {
    User user = service.readOne(id);

    if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return user;
  }

  @PutMapping("/{id}")
  public void updateOne(@PathVariable Integer id, @RequestBody UserWithCredentials user) {
    if (!Objects.equals(user.getId(), id)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    if (user.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    boolean updated = service.updateOne(user);
    if (!updated) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
  }

  @DeleteMapping("/{id}")
  public void deleteOne(@PathVariable Integer id) {
    boolean deleted = service.deleteOne(id);
    if (!deleted) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
  }
}