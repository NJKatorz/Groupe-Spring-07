package be.vinci.ipl.projet2024.group07.gateway.data;

import be.vinci.ipl.projet2024.group07.gateway.models.Attack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "attacks")
public interface AttacksProxy {

  @GetMapping("/attacks")
  Iterable<Attack> readAll();

  @PostMapping("/attacks")
  void createOne(@RequestBody Attack attack);

  @GetMapping("/attacks/{attackId}")
  Attack readOne(@PathVariable int attackId);

  @DeleteMapping("/attacks/{attackId}")
  void deleteOne(@PathVariable int attackId);

  @PatchMapping("/attacks/{attackId}/notes")
  void updateNotesAttack(@PathVariable int attackId, @RequestBody String notes);

  @PatchMapping("/attacks/{attackId}/server")
  void addServerToAttack(@PathVariable int attackId, @RequestBody int serverId);

  @PatchMapping("/attacks/{attackId}/exploit")
  void addExploitToAttack(@PathVariable int attackId, @RequestBody int exploitId);

  @PostMapping("/attacks/{attackId}/launch")
  void lauchAttack(@PathVariable int attackId);

  @PostMapping("/attacks/{attackId}/result")
  void saveAttackResult(@PathVariable int attackId, @RequestBody String result);
}
