package be.vinci.ipl.projet2024.group07.mockauthentications;

import be.vinci.ipl.projet2024.group07.mockauthentications.Credentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
public class AuthenticationsController {

  @PostMapping("/auth/register")
  public ResponseEntity<Void> register( @RequestBody Credentials credentials) {
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping("/auth/login")
  public ResponseEntity<String> login(@RequestBody Credentials credentials) {
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTIzNDU2Nzg5LCJuYW1lIjoiSm9zZXBoIn0.OpOSSw7e485LOP5PrzScxHb7SR6sAOMRckfFwi4rp7o";
    return new ResponseEntity<>(token, HttpStatus.OK);
  }

  @PostMapping("/auth/verify-token")
  public ResponseEntity<String> verifyToken(@RequestBody String token) {
    String email = "john.doe@example.com";
    return new ResponseEntity<>(email, HttpStatus.OK);
  }

  @PatchMapping("/auth/change-password")
  public ResponseEntity<Void> changePassword(@RequestBody Credentials request, @RequestParam String newPassword) {

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/auth/delete")
  public ResponseEntity<Void> delete(@RequestBody String email) {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
