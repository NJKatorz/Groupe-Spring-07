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

  /**
   * Convertit un UserWithCredentials en User sans les credentials.
   * @return un objet User
   */
  public User toUser() {
    return new User(0, name, email, "user");
  }

  /**
   * Convertit les credentials pour un service d'authentification.
   * @return un objet Credentials
   */
  public Credentials toCredentials() {
    return new Credentials(email, password);
  }

  /**
   * Vérifie si l'objet UserWithCredentials est invalide.
   * @return true si un champ requis est manquant ou vide
   */
  public boolean invalid() {
    return name == null || name.isBlank() ||
        email == null || email.isBlank() ||
        password == null || password.isBlank();
  }
}
