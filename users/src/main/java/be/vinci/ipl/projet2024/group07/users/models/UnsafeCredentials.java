package be.vinci.ipl.projet2024.group07.users.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class UnsafeCredentials {
  private int id;
  private String name;
  private String email;
  private String password;

  private static final int MIN_PASSWORD_LENGTH = 8;

  public SafeCredentials makeSafe(String hashedPassword) {
    return new SafeCredentials(email, hashedPassword);
  }

  public User toUser() {
    return new User(0, name,email, "user");
  }

  // pour les tests d'autentification
  public User toAdmin() {
    return new User(0, name,email, "admin");
  }

  public boolean invalid() {
    return email == null || email.isBlank() ||
        password == null || password.isBlank();
  }

  public boolean isPasswordTooShort() {
    return password != null && password.length() < MIN_PASSWORD_LENGTH;
  }
}