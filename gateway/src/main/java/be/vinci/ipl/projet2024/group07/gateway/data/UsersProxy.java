package be.vinci.ipl.projet2024.group07.gateway.data;

import be.vinci.ipl.projet2024.group07.gateway.models.Credentials;
import be.vinci.ipl.projet2024.group07.gateway.models.Exploit;
import be.vinci.ipl.projet2024.group07.gateway.models.User;
import be.vinci.ipl.projet2024.group07.gateway.models.UserWithCredentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(name = "users")
public interface UsersProxy {

  @GetMapping("/users")
  User getUserByEmail(@RequestParam String email);

  @PostMapping("/users")
  User createOne(@RequestBody UserWithCredentials credentials);

  @GetMapping("/users/{userId}")
  User readOneByUserId(@PathVariable int userId);

  @DeleteMapping("/users/{userId}")
  void deleteUser(@PathVariable int userId);

  @PatchMapping("/users/{userId}/name")
  void updateUserName(@PathVariable int userId, @RequestBody String newName);

  @PatchMapping("/users/{userId}/role")
  void updateUserRole(@PathVariable int userId, @RequestBody String newRole);

  @GetMapping("/exploits/author/{authorId}")
  Iterable<Exploit> getExploitsByAuthor(@PathVariable int authorId);
}
