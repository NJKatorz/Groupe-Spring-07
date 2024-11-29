package be.vinci.ipl.projet2024.group07.gateway.data;

import be.vinci.ipl.projet2024.group07.gateway.models.ChangePassword;
import be.vinci.ipl.projet2024.group07.gateway.models.Credentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(name = "authentications")
public interface AuthenticationProxy {

  /**
   * Connect a user
   * @param credentials the credentials of the user
   * @return the token
   */
  @PostMapping("/auth/login")
  String connect(@RequestBody Credentials credentials);

  /**
   * Verify a token
   * @param token the token to verify
   * @return the user's email
   */
  @PostMapping("/auth/verify-token")
  String verifyToken(@RequestBody String token);

  /**
   * Change the password of a user
   * @param usersWithNewPassword the user's email and the new password
   */
  @PatchMapping("/auth/change-password")
  void changePassword(@RequestBody ChangePassword usersWithNewPassword);
}
