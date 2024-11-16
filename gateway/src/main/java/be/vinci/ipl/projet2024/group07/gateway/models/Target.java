package be.vinci.ipl.projet2024.group07.gateway.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Target {
  private int targetId;
  private String companyName;
  private String description;
  private String domain;
  private String location;
  private int employees;
  private String servers;
  private String revenue;
}
