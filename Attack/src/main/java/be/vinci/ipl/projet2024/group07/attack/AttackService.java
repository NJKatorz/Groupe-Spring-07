package be.vinci.ipl.projet2024.group07.attack;

import be.vinci.ipl.projet2024.group07.attack.Attack.Status;
import java.util.Optional;

public class AttackService {

  private final AttackRepository attackRepository;

  public AttackService(AttackRepository attackRepository) {
    this.attackRepository = attackRepository;
  }

  public Iterable<Attack> readAll (){
    return attackRepository.findAll();
  }

  public Optional<Attack> readOne(int id){
    return attackRepository.findById(id);
  }

  public void createOne (Attack attack){
    attack.setStatus(Status.PlANIFIEE);
    attackRepository.save(attack);
  }

  public boolean deleteOne(int id){
    if(!attackRepository.existsById(id))
      return false;
    attackRepository.deleteById(id);
    return true;
  }

  public void updateNotes(String newNotes, int id){
    Optional<Attack> optionalAttack = readOne(id);
    Attack attack = optionalAttack.get();
    attack.setNotes(newNotes);
    attackRepository.save(attack);
  }

  public void updateServer(int serverId, int id){
    Optional<Attack> optionalAttack = readOne(id);
    Attack attack = optionalAttack.get();
    attack.setServerId(serverId);
    attackRepository.save(attack);
  }

  public void updateExploit(int exploitId, int id){
    Optional<Attack> optionalAttack = readOne(id);
    Attack attack = optionalAttack.get();
    attack.setServerId(exploitId);
    attackRepository.save(attack);
  }

  public void updateStatus(Status status, int id){
    Optional<Attack> optionalAttack = readOne(id);
    Attack attack = optionalAttack.get();
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
      if(attack.getServerId()==serverId){
        deleteOne(attack.getId());
      }
    }
    return true;
  }

  public boolean deleteExploits(int exploitId){
    Iterable<Attack> attacks = readAll();
    for (Attack attack : attacks) {
      if(attack.getExploitId()==exploitId){
        deleteOne(attack.getId());
      }
    }
    return true;
  }

}
