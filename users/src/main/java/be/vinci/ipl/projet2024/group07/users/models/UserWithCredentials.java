package be.vinci.ipl.projet2024.group07.users.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserWithCredentials {
  private int id;
  private String name;
  private String email;
  private String password;

  public User toUser() {
    return new User(0, name, email, "user");
  }

  public Credentials toCredentials() {
    return new Credentials(email, password);
  }

  public boolean invalid() {
    return name == null || name.isBlank() ||
        email == null || email.isBlank() ||
        password == null || password.isBlank();
  }
}
