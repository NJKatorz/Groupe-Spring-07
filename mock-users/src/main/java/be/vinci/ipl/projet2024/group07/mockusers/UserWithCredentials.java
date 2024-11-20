package be.vinci.ipl.projet2024.group07.mockusers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserWithCredentials {
  private String username;
  private String firstname;
  private String lastname;
  private String password;

  public User toUser() {
    return new User(username, firstname, lastname);
  }

  public Credentials toCredentials() {
    return new Credentials(username, password);
  }

  public boolean invalid() {
    return username == null || username.isBlank() ||
        firstname == null || firstname.isBlank() ||
        lastname == null || lastname.isBlank() ||
        password == null || password.isBlank();
  }

}
