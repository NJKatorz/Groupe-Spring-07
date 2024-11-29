package be.vinci.ipl.projet2024.group07.gateway.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Attack {
  private int id;
  private int targetId;
  private int serverId;
  private int exploitId;
  private String status;
  private String notes;

//  private enum Status {
//    PENDING, IN_PROGRESS, SUCCESS, FAILED;
//  }

}
