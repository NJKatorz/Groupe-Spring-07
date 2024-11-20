package be.vinci.ipl.projet2024.group07.mockusers;

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

public class User {
  private String pseudo;
  private String firstname;
  private String lastname;
}
