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
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(name = "users")
public interface UsersProxy {
  @GetMapping("/users")
  User readOneByEmail(@RequestParam(value = "email") String email);

  @PostMapping("/users")
  void createOne(@RequestBody Credentials credentials);

  @GetMapping("/users/{userId}")
  User readOneByUserId(@PathVariable int userId);

  @DeleteMapping("/users/{usersId}")
  void deleteOne(@PathVariable int usersId);

  @PatchMapping("/users/{userId}/name")
  void updateName(@PathVariable int usersId, @RequestBody String name);

  @PatchMapping("/users/{usersId}/role")
  void updateRole(@PathVariable int usersId, @RequestBody String role);

  @PatchMapping("/users/{userId}/password")
  void updatePassword(@PathVariable int usersId, @RequestBody Credentials credentials);

  @GetMapping("/users/{userId}/exploits")
  Iterable<Exploit> readAllUserExploits(@PathVariable int usersId);
}
