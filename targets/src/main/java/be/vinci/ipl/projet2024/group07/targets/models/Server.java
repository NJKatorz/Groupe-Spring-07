package be.vinci.ipl.projet2024.group07.targets.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Server {
  private int id ;
  private String ipAddress ;
  private int targetId;
  private String serverType;
  private String technology;
  private boolean validated;
}
