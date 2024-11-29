package be.vinci.ipl.projet2024.group07.attack.models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Server {
  private int id;
  private String ipAddress;
  private int targetId;
  private String serverType;
  private String technology;
  private boolean validated;
}
