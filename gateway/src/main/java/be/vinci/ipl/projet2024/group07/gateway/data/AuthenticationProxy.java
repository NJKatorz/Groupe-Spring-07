package be.vinci.ipl.projet2024.group07.gateway.data;

import be.vinci.ipl.projet2024.group07.gateway.models.Credentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "authentication")
public class AuthenticationProxy {

  @PostMapping("/authentication/connect")
  String connect(@RequestBody Credentials credentials);

  @PostMapping("/authentication/verify")
  String verify(@RequestBody String token);
}
