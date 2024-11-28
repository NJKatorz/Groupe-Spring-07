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
  public ResponseEntity<String> createOne(@RequestBody int targetId){
    try{
      attackService.createOne(targetId);
      return new ResponseEntity<>(HttpStatus.CREATED);
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
  public void deleteOne(@PathVariable int attackId){
    try{
      attackService.deleteOne(attackId);
    }
    catch (Exception e){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  @PatchMapping("/attack/{attackId}/notes")
  public void updateNotes(@PathVariable int attackId, @RequestBody String notes){
    try{
      attackService.updateNotes(notes, attackId);
    }
    catch (Exception e){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  @PatchMapping("/attack/{attackId}/server")
  public void updateServer(@PathVariable int attackId,@RequestBody int serverId){
    Attack attack = attackService.readOne(attackId);
    if (attack == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attaque non trouvé");
    try{
      attackService.updateServer(serverId, attackId);
    }
    catch (Exception e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @PatchMapping("/attack/{attackId}/exploit")
  public void updateExploit(@PathVariable int attackId, @RequestBody int exploitId){
    Attack attack = attackService.readOne(attackId);
    if (attack == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attaque non trouvé");
    try{
      attackService.updateExploit(exploitId, attackId);
    }
    catch (Exception e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @PostMapping("/attack/{attackId}/launch")
  public void launchAttack(@PathVariable int attackId){
    Attack attack = attackService.readOne(attackId);
    if (attack == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attaque non trouvé");
    if(attack.getStatus() != Status.PLANIFIEE || attack.getServerId()==null || attack.getExploitId()==null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Serveur ou exploit non défini ou attaque déjà lancée");
    attackService.updateStatus(Status.EN_COURS, attackId);
  }

  @PostMapping("/attack/{attackId}/result")
  public void resultAttack(@PathVariable int attackId, Status status){
    Attack attack = attackService.readOne(attackId);
    if (attack == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attaque non trouvé");
    if(attack.getStatus() != Status.EN_COURS || (status != Status.TERMINEE && status != Status.ECHOUEE))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'attaque n'était pas en cours, ou le résultat fourni est incorrect");
    attackService.updateStatus(status, attackId);
  }

  @DeleteMapping("/attack/targets/{targetId}")
  public void deleteTargets(@PathVariable int targetId){
    attackService.deleteTargets(targetId);
  }

  @DeleteMapping("/attack/servers/{serverId}")
  public void deleteServers(@PathVariable int serverId){
    attackService.deleteServers(serverId);
  }


  @DeleteMapping("/attack/exploits/{exploitId}")
  public void deleteExploits(@PathVariable int exploitId){
    attackService.deleteExploits(exploitId);
  }


}
