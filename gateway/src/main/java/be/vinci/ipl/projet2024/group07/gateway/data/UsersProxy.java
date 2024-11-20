package be.vinci.ipl.projet2024.group07.gateway.data;

import be.vinci.ipl.projet2024.group07.gateway.models.Credentials;
import be.vinci.ipl.projet2024.group07.gateway.models.Exploit;
import be.vinci.ipl.projet2024.group07.gateway.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "users")
public interface UsersProxy {
  @GetMapping("/users/{email}")
  User findByEmail(@PathVariable("email") String email);

  @PostMapping("/users")
  void createUser(@RequestBody Credentials credentials);

  @GetMapping("/users/{userId}")
  User findByUserId(@PathVariable("userId") String userId);

  @DeleteMapping("/users/{usersId}")
  void deleteUser(@PathVariable("userId") int usersId);

  @PatchMapping("/users/{userId}/name")
  void updateUserName(@PathVariable("userId") int usersId, @RequestBody Credentials credentials);

  @PatchMapping("/users/{userId}/password")
  void updateUserPassword(@PathVariable("userId") int usersId, @RequestBody Credentials credentials);

  @PatchMapping("/users/{usersId}/role")
  void updateUserRole(@PathVariable("userId") int usersId, @RequestBody Credentials credentials);

  @GetMapping("/users/{userId}/exploits")
  Iterable<Exploit> getUserExploits(@PathVariable("userId") int usersId);
}
