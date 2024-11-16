package be.vinci.ipl.projet2024.group07.gateway.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {
  private int userId;
  private String name;
  private String email;
  private Role role;

  private enum Role {
    ADMIN, USER;
  }
}
