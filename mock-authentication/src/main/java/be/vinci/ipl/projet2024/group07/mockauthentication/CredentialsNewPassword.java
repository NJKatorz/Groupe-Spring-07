package be.vinci.ipl.projet2024.group07.mockauthentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsNewPassword {
  private String email;
  private String oldPassword;
  private String newPassword;
}
