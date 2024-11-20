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
  private int id;
  private String companyName;
  private String domain;
  private String location;
  private int employees;
  private int servers;
  private double revenue;
}
