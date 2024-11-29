package be.vinci.ipl.projet2024.group07.attack;


import be.vinci.ipl.projet2024.group07.attack.models.Attack;
import be.vinci.ipl.projet2024.group07.attack.models.Attack.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AttackControler {

  private final AttackService attackService;

  public AttackControler(AttackService attackService){
    this.attackService = attackService;
  }


  @GetMapping("/attack")
  public Iterable<Attack> getAllAttacks(){
      return attackService.readAll();
  }

  @PostMapping("/attack")
  public ResponseEntity<String> createOne(@RequestBody Attack attack){
    try{
      attackService.createOne(attack);
      return ResponseEntity.status(HttpStatus.CREATED).body("L'attaque a été créée");
    }
    catch (Exception e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @GetMapping("/attack/{attackId}")
  public Attack readOne(@PathVariable int attackId){
    Attack attack = attackService.readOne(attackId);
    if (attack == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune attaque trouvée pour cet ID");
    return attack;
  }

  @DeleteMapping("/attack/{attackId}")
  public ResponseEntity<Void> deleteOne(@PathVariable int attackId){
      if(attackService.readOne(attackId) == null)
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune attaque trouvée pour cet ID");
      attackService.deleteOne(attackId);
      return ResponseEntity.noContent().build(); // 204 No Content
  }

  @PatchMapping("/attack/{attackId}/notes")
  public ResponseEntity<Void> updateNotes(@PathVariable int attackId, @RequestBody String notes){
    Attack attack = attackService.readOne(attackId);
    if (attack == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune attaque trouvée pour cet ID");
    attackService.updateNotes(notes, attackId);
    return ResponseEntity.noContent().build();

  }

  @PatchMapping("/attack/{attackId}/server")
  public ResponseEntity<Void> updateServer(@PathVariable int attackId,@RequestBody int serverId){
    Attack attack = attackService.readOne(attackId);
    if (attack == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attaque non trouvé");
    try{
      attackService.updateServer(serverId, attackId);
      return ResponseEntity.noContent().build();
    }
    catch (Exception e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @PatchMapping("/attack/{attackId}/exploit")
  public ResponseEntity<Void> updateExploit(@PathVariable int attackId, @RequestBody int exploitId){
    Attack attack = attackService.readOne(attackId);
    if (attack == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attaque non trouvé");
    try{
      attackService.updateExploit(exploitId, attackId);
      return ResponseEntity.noContent().build();
    }
    catch (Exception e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @PostMapping("/attack/{attackId}/launch")
  public ResponseEntity<Void> launchAttack(@PathVariable int attackId){
    Attack attack = attackService.readOne(attackId);
    if (attack == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune attaque trouvée pour cet ID");
    if(attack.getStatus() != Status.PLANIFIEE || attack.getServerId()==null || attack.getExploitId()==null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Serveur ou exploit non défini ou attaque déjà lancée");
    attackService.updateStatus(Status.EN_COURS, attackId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/attack/{attackId}/result")
  public ResponseEntity<Void> resultAttack(@PathVariable int attackId, @RequestBody Status status){
    Attack attack = attackService.readOne(attackId);
    if (attack == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attaque non trouvé");
    if(attack.getStatus() != Status.EN_COURS || (status != Status.TERMINEE && status != Status.ECHOUEE))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'attaque n'était pas en cours, ou le résultat fourni est incorrect");
    attackService.updateStatus(status, attackId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/attack/targets/{targetId}")
  public ResponseEntity<Void> deleteTargets(@PathVariable int targetId){
    attackService.deleteTargets(targetId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/attack/servers/{serverId}")
  public ResponseEntity<Void> deleteServers(@PathVariable int serverId){
    attackService.deleteServers(serverId);
    return ResponseEntity.noContent().build();
  }


  @DeleteMapping("/attack/exploits/{exploitId}")
  public ResponseEntity<Void> deleteExploits(@PathVariable int exploitId){
    attackService.deleteExploits(exploitId);
    return ResponseEntity.noContent().build();
  }


}
