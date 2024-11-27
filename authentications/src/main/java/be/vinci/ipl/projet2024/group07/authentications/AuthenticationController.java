package be.vinci.ipl.projet2024.group07.authentications;

import be.vinci.ipl.projet2024.group07.authentications.AuthenticationService;
import be.vinci.ipl.projet2024.group07.authentications.models.UnsafeCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
public class AuthenticationController {

  private final AuthenticationService service;

  public AuthenticationController(AuthenticationService service) {
    this.service = service;
  }

  @PostMapping("/auth/register")
  public ResponseEntity<Void> register( @RequestBody UnsafeCredentials credentials) {
    if(credentials.invalid()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials");
    }
    boolean register =service.createOne(credentials);
    if (!register){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping("/auth/login")
  public ResponseEntity<String> login(@RequestBody UnsafeCredentials credentials) {
    if (credentials.invalid()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials");
    }
    String token = service.connect(credentials);
    if (token == null){
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }
    return new ResponseEntity<>(token, HttpStatus.OK);
  }

  @PostMapping("/auth/verify-token")
  public ResponseEntity<String> verifyToken(@RequestBody String token) {
    if (Objects.isNull(token)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
    }
    String email = service.verify(token);
    if (Objects.isNull(email)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
    }
    return new ResponseEntity<>(email, HttpStatus.OK);
  }

  @PatchMapping("/auth/change-password")
  public ResponseEntity<Void> changePassword(@RequestBody UnsafeCredentials request, @RequestParam String newPassword) {
    if (request.invalid() || newPassword == null || newPassword.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials");
    }
    boolean changed = service.changePassword(request, newPassword);
    if (!changed) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }//Pas sur de la methode

  @DeleteMapping("/auth/delete")
  public ResponseEntity<Void> delete(@RequestBody String email) {
    if (email == null || email.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");
    }
    boolean deleted = service.deleteOne(email);
    if(!deleted) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}