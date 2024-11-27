package be.vinci.ipl.projet2024.group07.authentications.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePassword {
  private String email;
  private String password;
  private String newPassword;


  public boolean invalid() {
    return email == null || email.isBlank() ||
        password == null || password.isBlank();
  }

  public SafeCredentials makeSafe(String hashedPassword) {
    return new SafeCredentials(email, hashedPassword);
  }
}
