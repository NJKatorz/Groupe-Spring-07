package be.vinci.ipl.projet2024.group07.attack;

import be.vinci.ipl.projet2024.group07.attack.models.Attack;
import be.vinci.ipl.projet2024.group07.attack.models.Attack.Status;
import be.vinci.ipl.projet2024.group07.attack.repositories.AttackRepository;
import be.vinci.ipl.projet2024.group07.attack.repositories.ExploitProxy;
import be.vinci.ipl.projet2024.group07.attack.repositories.ServerProxy;
import be.vinci.ipl.projet2024.group07.attack.repositories.TargetProxy;
import org.springframework.stereotype.Service;

@Service
public class AttackService {

  private final AttackRepository attackRepository;
  private final TargetProxy targetProxy;
  private final ServerProxy serverProxy;

  private final ExploitProxy exploitProxy;

  public AttackService(AttackRepository attackRepository, TargetProxy targetProxy,
      ServerProxy serverProxy, ExploitProxy exploitProxy) {
    this.attackRepository = attackRepository;
    this.targetProxy = targetProxy;
    this.serverProxy = serverProxy;
    this.exploitProxy = exploitProxy;
  }

  /**
   * Récupère toutes les attaques.
   * @return une liste d'attaques.
   */
  public Iterable<Attack> readAll (){
    return attackRepository.findAll();
  }

  /**
   * Récupère une attaque en fonction de son ID.
   * @param id l'ID de l'attaque
   * @return l'attaque correspondante.
   */
  public Attack readOne(int id){
    return attackRepository.findById(id).orElse(null);
  }

  /**
   * Crée une nouvelle attaque.
   * @param attack l'attaque à créer.
   * @return l'attaque
   * @throws IllegalArgumentException si les données de l'attaque sont manquantes ou incorrectes
   */
  public Attack createOne (Attack attack){
    if(targetProxy.readOne(attack.getTargetId())==null)
      throw new IllegalArgumentException("Les données de l'attaque sont manquantes ou incorrectes");
    attack.setStatus(Status.PLANIFIEE.toString());
    return attackRepository.save(attack);
  }

  /**
   * Supprime une attaque en fonction de son ID.
   * @param id l'ID de l'attaque à supprimer.
   * @throws IllegalArgumentException si aucune attaque n'est trouvée pour cet ID
   */
  public void deleteOne(int id){
    if(!attackRepository.existsById(id))
      throw new IllegalArgumentException("Aucune attaque trouvée pour cet ID");
    attackRepository.deleteById(id);
  }

  /**
   * Met à jour les notes d'une attaque.
   * @param newNotes les nouvelles notes.
   * @param id l'ID de l'attaque à mettre à jour.
   */
  public void updateNotes(String newNotes, int id){
    Attack attack = readOne(id);
    attack.setNotes(newNotes);
    attackRepository.save(attack);
  }


  /**
   * Met à jour le serveur d'une attaque.
   *
   * @param serverId l'ID du serveur à mettre à jour.
   * @param id l'ID de l'attaque à mettre à jour.
   * @throws IllegalArgumentException si le serveur n'existe pas ou n'est pas validé.
   */
  public void updateServer(int serverId, int id){
    if(serverProxy.readOne(serverId)==null || !serverProxy.readOne(serverId).isValidated())
      throw new IllegalArgumentException("Serveur non existant ou validé");
    Attack attack = readOne(id);
    attack.setServerId(serverId);
    attackRepository.save(attack);
  }


  /**
   * Met à jour l'exploit d'une attaque.
   *
   * @param exploitId l'ID de l'exploit à mettre à jour.
   * @param id l'ID de l'attaque à mettre à jour.
   * @throws IllegalArgumentException si l'exploit n'existe pas ou n'est pas validé.
   */
  public void updateExploit(int exploitId, int id){
    if(exploitProxy.getExploit(exploitId)==null || !exploitProxy.getExploit(exploitId).isValidated())
      throw new IllegalArgumentException("Exploit non existant ou validé");
    Attack attack = readOne(id);
    attack.setExploitId(exploitId);
    attackRepository.save(attack);
  }


  /**
   * Met à jour le statut d'une attaque.
   *
   * @param status le nouveau statut de l'attaque.
   * @param id l'ID de l'attaque à mettre à jour.
   */
  public void updateStatus(String status, int id){
    Attack attack = readOne(id);
    attack.setStatus(status);
    attackRepository.save(attack);
  }

  /**
   * Supprime toutes les attaques ciblant une cible.
   *
   * @param targetId l'ID de la cible.
   */
  public void deleteTargets(int targetId){
    Iterable<Attack> attacks = readAll();
    for (Attack attack : attacks) {
      if(attack.getTargetId()==targetId){
        deleteOne(attack.getId());
      }
    }
  }

  /**
   * Supprime toutes les attaques ciblant un serveur.
   *
   * @param serverId l'ID du serveur.
   */
  public void deleteServers(int serverId){
    Iterable<Attack> attacks = readAll();
    for (Attack attack : attacks) {
      if(attack.getServerId()!=null && attack.getServerId().intValue()==serverId){
        deleteOne(attack.getId());
      }
    }
  }

  /**
   * Supprime toutes les attaques ciblant un exploit.
   *
   * @param exploitId l'ID de l'exploit.
   */
  public void deleteExploits(int exploitId){
    Iterable<Attack> attacks = readAll();
    for (Attack attack : attacks) {
      if(attack.getExploitId()!=null && attack.getExploitId().intValue()==exploitId){
        deleteOne(attack.getId());
      }
    }
  }

}
