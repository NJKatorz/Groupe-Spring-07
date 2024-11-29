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

  public SafeCredentials makeSafe(String hashedPassword) {
    return new SafeCredentials(email, hashedPassword);
  }

  public User toUser() {
    return new User(0, name,email, password, "user");
  }

  public boolean invalid() {
    return email == null || email.isBlank() ||
        password == null || password.isBlank();
  }
}