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

  @PostMapping("/auth/login")
  String connect(@RequestBody Credentials credentials);

  @PostMapping("/auth/verify-token")
  String verifyToken(@RequestBody String token);

  @PatchMapping("/auth/change-password")
  void changePassword(@RequestBody ChangePassword usersWithNewPassword);
}
