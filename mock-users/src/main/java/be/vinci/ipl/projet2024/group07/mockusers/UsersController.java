package be.vinci.ipl.projet2024.group07.mockusers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {
  @PostMapping("/{username}")
  public ResponseEntity<Void> createUser(@PathVariable String username, @RequestBody UserWithCredentials user) {
    if (user.invalid() || !user.getUsername().equals(username)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/{username}")
  public ResponseEntity<User> getUser(@PathVariable String username) {
    User user = new User(username, "John", "Doe");
    return ResponseEntity.ok(user);
  }

  @PutMapping("/{username}")
  public ResponseEntity<Void> updateUser(@PathVariable String username, @RequestBody UserWithCredentials user) {
    if (user.invalid() || !user.getUsername().equals(username)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<Void> deleteUser(@PathVariable String username) {
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
