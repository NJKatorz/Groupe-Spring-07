package be.vinci.ipl.projet2024.group07.mockattacks;

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
    private Integer serverId;
    private Integer exploitId;
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
