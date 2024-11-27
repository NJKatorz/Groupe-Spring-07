package be.vinci.ipl.projet2024.group07.mockusers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsersController {

  private final Map<Integer, User> users = new HashMap<>();
  private int currentId = 1;

  @GetMapping
  public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
    User user = users.values().stream()
        .filter(u -> u.getEmail().equalsIgnoreCase(email))
        .findFirst()
        .orElse(null);

    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return ResponseEntity.ok(user);
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody UserWithCredentials user) {
    User newUser = new User();
    newUser.setId(currentId++);
    newUser.setName(user.getName());
    newUser.setEmail(user.getEmail());
    newUser.setRole("user");

    users.put(newUser.getId(), newUser);
    return new ResponseEntity<>(newUser, HttpStatus.CREATED);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<User> getUserById(@PathVariable int userId) {
    User user = users.get(userId);

    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return ResponseEntity.ok(user);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
    if (!users.containsKey(userId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    users.remove(userId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PatchMapping("/{userId}/name")
  public ResponseEntity<Void> updateUserName(@PathVariable int userId, @RequestBody String newName) {
    User user = users.get(userId);

    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    user.setName(newName);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PatchMapping("/{userId}/role")
  public ResponseEntity<Void> updateUserRole(@PathVariable int userId, @RequestBody String newRole) {
    User user = users.get(userId);

    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    user.setRole(newRole);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
