package be.vinci.ipl.projet2024.group07.gateway.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithCredentials {
  private String name;
  private String email;
  private String password;
}