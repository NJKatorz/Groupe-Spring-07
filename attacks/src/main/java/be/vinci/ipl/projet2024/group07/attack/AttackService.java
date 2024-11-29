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

  public Iterable<Attack> readAll (){
    return attackRepository.findAll();
  }

  public Attack readOne(int id){
    return attackRepository.findById(id).orElse(null);
  }

  public Attack createOne (Attack attack){
    if(targetProxy.readOne(attack.getTargetId())==null)
      throw new IllegalArgumentException("Les données de l'attaque sont manquantes ou incorrectes");
    attack.setStatus(Status.PLANIFIEE);
    return attackRepository.save(attack);
  }


  public void deleteOne(int id){
    if(!attackRepository.existsById(id))
      throw new IllegalArgumentException("Aucune attaque trouvée pour cet ID");
    attackRepository.deleteById(id);
  }

  public void updateNotes(String newNotes, int id){
    Attack attack = readOne(id);
    attack.setNotes(newNotes);
    attackRepository.save(attack);
  }

  public void updateServer(int serverId, int id){
    if(serverProxy.readOne(serverId)==null || !serverProxy.readOne(serverId).isValidated())
      throw new IllegalArgumentException("Serveur non existant ou validé");
    Attack attack = readOne(id);
    attack.setServerId(serverId);
    attackRepository.save(attack);
  }

  public void updateExploit(int exploitId, int id){
    if(exploitProxy.getExploit(exploitId)==null || !exploitProxy.getExploit(exploitId).isValidated())
      throw new IllegalArgumentException("Exploit non existant ou validé");
    Attack attack = readOne(id);
    attack.setExploitId(exploitId);
    attackRepository.save(attack);
  }

  public void updateStatus(Status status, int id){
    Attack attack = readOne(id);
    attack.setStatus(status);
    attackRepository.save(attack);
  }

  public boolean deleteTargets(int targetId){
    Iterable<Attack> attacks = readAll();
    for (Attack attack : attacks) {
      if(attack.getTargetId()==targetId){
        deleteOne(attack.getId());
      }
    }
    return true;
  }

  public boolean deleteServers(int serverId){
    Iterable<Attack> attacks = readAll();
    for (Attack attack : attacks) {
      if(attack.getServerId()!=null && attack.getServerId().intValue()==serverId){
        deleteOne(attack.getId());
      }
    }
    return true;
  }

  public boolean deleteExploits(int exploitId){
    Iterable<Attack> attacks = readAll();
    for (Attack attack : attacks) {
      if(attack.getExploitId()!=null && attack.getExploitId().intValue()==exploitId){
        deleteOne(attack.getId());
      }
    }
    return true;
  }

}
