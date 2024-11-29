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

  /**
   * Get all users
   * @return all users
   */
  @GetMapping("/users")
  User getUserByEmail(@RequestParam String email);

  /**
   * Create a user
   * @param credentials the credentials of the user
   * @return the created user
   */
  @PostMapping("/users")
  User createOne(@RequestBody UserWithCredentials credentials);

  /**
   * Get one user
   * @param userId the id of the user to get
   * @return the user
   */
  @GetMapping("/users/{userId}")
  User readOneByUserId(@PathVariable int userId);

  /**
   * Delete one user
   * @param userId the id of the user to delete
   */
  @DeleteMapping("/users/{userId}")
  void deleteUser(@PathVariable int userId);

  /**
   * Update the name of a user
   * @param userId the id of the user to update
   * @param newName the new name
   */
  @PatchMapping("/users/{userId}/name")
  void updateUserName(@PathVariable int userId, @RequestBody String newName);

  /**
   * Update the role of a user
   * @param userId the id of the user to update
   * @param newRole the new role
   */
  @PatchMapping("/users/{userId}/role")
  void updateUserRole(@PathVariable int userId, @RequestBody String newRole);

  /**
   * Get all exploits by author
   * @param authorId the id of the author
   * @return all exploits by author
   */
  @GetMapping("/exploits/author/{authorId}")
  Iterable<Exploit> getExploitsByAuthor(@PathVariable int authorId);

  /**
   * Create an admin
   * @param credentials the credentials of the admin
   * @return the created admin
   */
  // for the tests of the authentification
  @PostMapping("/users/admin")
  User createAdmin(@RequestBody UserWithCredentials credentials);
}
