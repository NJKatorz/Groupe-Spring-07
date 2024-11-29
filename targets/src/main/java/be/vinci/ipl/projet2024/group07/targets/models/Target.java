package be.vinci.ipl.projet2024.group07.targets.models;

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
@Entity(name = "targets")
public class Target {
  @Id @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(nullable = false)
  private String companyName;
  @Column(nullable = false)
  private String domain;
  @Column(nullable = false)
  private String location;
  @Column(nullable = false)
  private int employees;
  @Column(nullable = false)
  private int servers;
  @Column(nullable = false)
  private int revenue;

  /**
   * Vérifie si la cible contient toutes les informations nécéssaires.
   * @return true si la cible est valide, false dans le cas contraire.
   */
  public boolean invalid() {
    return companyName == null || companyName.isBlank() ||
        domain == null || domain.isBlank() ||
        location == null || location.isBlank() ||
        employees <= 0 || servers < 0 || revenue <= 0;
  }
}
