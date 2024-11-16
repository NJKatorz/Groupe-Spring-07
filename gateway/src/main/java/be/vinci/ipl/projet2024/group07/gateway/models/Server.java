package be.vinci.ipl.projet2024.group07.gateway.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Server {
  private int serverId;
  private String ipAdress;
  private int targetId;
  private ServerType serverType;
  private String technology;
  private boolean validated;

  private enum ServerType {
    WEB, DATABASE, BACKUP, MAIL, WORKSTATION;
  }
}
