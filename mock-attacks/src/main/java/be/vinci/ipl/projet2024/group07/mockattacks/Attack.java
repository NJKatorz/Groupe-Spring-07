package be.vinci.ipl.projet2024.group07.mockattacks;

import java.lang.annotation.Target;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Attack {
    private int attackId;
    private int targetId;
    private int serverId;
    private int exploitId;
    private String status;
    private String notes;
}
