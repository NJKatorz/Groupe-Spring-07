package be.vinci.ipl.projet2024.group07.servers.models;

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
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "servers")
public class Server {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private int id;
  @Column(nullable = false)
  private String ipAdress;
  @Column(nullable = false)
  private int targetId;
  @Column(nullable = false)
  private String serverType;
  @Column(nullable = false)
  private String technology;
  @Column(nullable = false)
  private boolean validated;
}
