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
  private int id;
  private String name;
  private String email;
  private String role;

//  private enum Role {
//    ADMIN, USER;
//  }
}
