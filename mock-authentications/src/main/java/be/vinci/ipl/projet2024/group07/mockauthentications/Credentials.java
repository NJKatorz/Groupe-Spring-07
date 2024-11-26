package be.vinci.ipl.projet2024.group07.mockauthentications;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Credentials {
  private String email;
  private String password;
}
