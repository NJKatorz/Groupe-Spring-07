package be.vinci.ipl.projet2024.group07.authentications.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UnsafeCredentials {
  private String email;
  private String password;

  public SafeCredentials makeSafe(String hashedPassword) {
    return new SafeCredentials(email, hashedPassword);
  }

  public boolean invalid() {
    return email == null || email.isBlank() ||
        password == null || password.isBlank();
  }
}