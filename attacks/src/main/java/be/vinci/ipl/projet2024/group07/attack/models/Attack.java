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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "attacks")
public class Attack {
  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(nullable = false)
  private int targetId;
  private Integer serverId;
  private Integer exploitId;
  @Column(nullable = false)
  private Status status;
  private String notes;

  public enum Status {
    PLANIFIEE("planifiée"),
    EN_COURS("en cours"),
    TERMINEE("terminée"),
    ECHOUEE("échouée");

    private final String value;

    Status(String value) {
      this.value = value;
    }
  }
}

