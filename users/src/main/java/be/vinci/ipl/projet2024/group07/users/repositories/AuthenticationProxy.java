package be.vinci.ipl.projet2024.group07.users.repositories;

import be.vinci.ipl.projet2024.group07.users.models.UnsafeCredentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authentications")
public interface AuthenticationProxy  {

  @DeleteMapping("/auth/delete")
  void delete (@RequestBody String email);

  @PostMapping("/auth/register")
  void register (UnsafeCredentials creditential);
}