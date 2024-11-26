package be.vinci.ipl.projet2024.group07.mockauthentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public class AuthenticationController {

  @PostMapping("/auth/login")
  public String connect(@RequestBody Credentials credentials) {
    return "JWT token";
  }


  @PostMapping("/auth/verify-token")
  public String verify(@RequestBody String token) {
    return "email";
  }

  @PostMapping("/auth/register")
  public ResponseEntity<Void> register(@RequestBody Credentials credentials) {
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PatchMapping("/auth/change-password")
  public void changePassword(@RequestBody CredentialsNewPassword credentials) {
  }

  @DeleteMapping("/auth/delete")
  public void deleteCredentials(@RequestBody String email) {
  }
}
