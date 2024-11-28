package be.vinci.ipl.projet2024.group07.attack.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Target {

  private int id;
  private String companyName;
  private String domain;
  private String location;
  private int employees;
  private int servers;
  private int revenue;

  public boolean invalid() {
    return companyName == null || companyName.isBlank() ||
        domain == null || domain.isBlank() ||
        location == null || location.isBlank() ||
        employees <= 0 || servers < 0 || revenue <= 0;
  }
}
