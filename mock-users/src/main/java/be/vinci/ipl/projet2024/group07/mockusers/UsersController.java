package be.vinci.ipl.projet2024.group07.mockusers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
  @PostMapping("/{username}")
  public ResponseEntity<Void> createOne(@PathVariable String username, @RequestBody UserWithCredentials user) {
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/{username}")
  public User readOne(@PathVariable String username) {
    User user = new User();
    user.setPseudo(username);
    user.setFirstname("test");
    user.setLastname("test");
    return user;
  }

  @PutMapping("/{username}")
  public void updateOne(@PathVariable String username, @RequestBody UserWithCredentials user) {
  }

  @DeleteMapping("/{username}")
  public void deleteOne(@PathVariable String username) {
  }
}
