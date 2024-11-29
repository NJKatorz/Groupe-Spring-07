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


  /**
   * Récupère toutes les attaques.
   * @return une liste d'attaques.
   */
  @GetMapping("/attack")
  public Iterable<Attack> getAllAttacks(){
      return attackService.readAll();
  }


  /**
   * Crée une nouvelle attaque.
   * @param attack l'attaque à créer.
   * @return une réponse contenant l'attaque créée.
   */
  @PostMapping("/attack")
  public ResponseEntity<Attack> createOne(@RequestBody Attack attack){
    try{
      Attack newAttack = attackService.createOne(attack);
      return new ResponseEntity<>(newAttack, HttpStatus.CREATED);
    }
    catch (Exception e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  /**
   * Récupère une attaque en fonction de son ID.
   * @param attackId l'ID de l'attaque à récupérer.
   * @return l'attaque correspondante.
   */
  @GetMapping("/attack/{attackId}")
  public Attack readOne(@PathVariable int attackId){
    Attack attack = attackService.readOne(attackId);
    if (attack == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune attaque trouvée pour cet ID");
    return attack;
  }

  /**
   * Supprime une attaque en fonction de son ID.
   * @param attackId l'ID de l'attaque à supprimer.
   * @return une réponse vide.
   */
  @DeleteMapping("/attack/{attackId}")
  public ResponseEntity<Void> deleteOne(@PathVariable int attackId){
      if(attackService.readOne(attackId) == null)
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune attaque trouvée pour cet ID");
      attackService.deleteOne(attackId);
      return ResponseEntity.noContent().build(); // 204 No Content
  }


  /**
   * Met à jour les notes d'une attaque.
   * @param attackId l'ID de l'attaque à mettre à jour.
   * @param notes les nouvelles notes à ajouter.
   * @return une réponse vide.
   */
  @PatchMapping("/attack/{attackId}/notes")
  public ResponseEntity<Void> updateNotes(@PathVariable int attackId, @RequestBody String notes){
    Attack attack = attackService.readOne(attackId);
    if (attack == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune attaque trouvée pour cet ID");
    attackService.updateNotes(notes, attackId);
    return ResponseEntity.noContent().build();
  }


  /**
   * Met à jour le serveur d'une attaque.
   * @param attackId l'ID de l'attaque à mettre à jour.
   * @param serverId le nouvel ID du serveur.
   * @return une réponse vide.
   * */
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


  /**
   * Met à jour l'exploit d'une attaque.
   * @param attackId l'ID de l'attaque à mettre à jour.
   * @param exploitId le nouvel ID de l'exploit.
   * @return une réponse vide.
   */
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

  /**
   * Lance une attaque.
   * @param attackId l'ID de l'attaque à lancer.
   * @return une réponse vide.
   */
  @PostMapping("/attack/{attackId}/launch")
  public ResponseEntity<Void> launchAttack(@PathVariable int attackId){
    Attack attack = attackService.readOne(attackId);
    if (attack == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune attaque trouvée pour cet ID");
    if(attack.getStatus() != Status.PLANIFIEE.toString() || attack.getServerId()==null || attack.getExploitId()==null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Serveur ou exploit non défini ou attaque déjà lancée");
    attackService.updateStatus(Status.EN_COURS.toString(), attackId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Met à jour le statut d'une attaque.
   * @param attackId l'ID de l'attaque à mettre à jour.
   * @param status le nouveau statut de l'attaque.
   * @return une réponse vide.
   */
  @PostMapping("/attack/{attackId}/result")
  public ResponseEntity<Void> resultAttack(@PathVariable int attackId, @RequestBody String status){
    Attack attack = attackService.readOne(attackId);
    if (attack == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attaque non trouvé");
    if(attack.getStatus() != Status.EN_COURS.toString() || (status != Status.TERMINEE.toString() && status != Status.ECHOUEE.toString()))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'attaque n'était pas en cours, ou le résultat fourni est incorrect");
    attackService.updateStatus(status, attackId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Supprime les attaques liées à une cible.
   * @param targetId l'ID de la cible.
   * @return une réponse vide.
   */
  @DeleteMapping("/attack/targets/{targetId}")
  public ResponseEntity<Void> deleteTargets(@PathVariable int targetId){
    attackService.deleteTargets(targetId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Supprime les attaques liées à un serveur.
   * @param serverId l'ID du serveur.
   * @return une réponse vide.
   */
  @DeleteMapping("/attack/servers/{serverId}")
  public ResponseEntity<Void> deleteServers(@PathVariable int serverId){
    attackService.deleteServers(serverId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Supprime les attaques liées à un exploit.
   * @param exploitId l'ID de l'exploit.
   * @return une réponse vide.
   */
  @DeleteMapping("/attack/exploits/{exploitId}")
  public ResponseEntity<Void> deleteExploits(@PathVariable int exploitId){
    attackService.deleteExploits(exploitId);
    return ResponseEntity.noContent().build();
  }


}
