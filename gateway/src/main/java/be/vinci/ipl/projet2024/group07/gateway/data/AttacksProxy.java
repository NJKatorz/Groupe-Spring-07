package be.vinci.ipl.projet2024.group07.gateway.data;

import be.vinci.ipl.projet2024.group07.gateway.models.Attack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
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

  /**
   * Get all attacks
   * @return all attacks
   */
  @GetMapping("/attack")
  Iterable<Attack> readAll();

  /**
   * Create an attack
   * @param attack the attack to create
   * @return the created attack
   */
  @PostMapping("/attack")
  ResponseEntity<Attack> createOne(@RequestBody Attack attack);

  /**
   * Get one attack
   * @param attackId the id of the attack to get
   * @return the attack
   */
  @GetMapping("/attack/{attackId}")
  Attack readOne(@PathVariable int attackId);

  /**
   * Delete one attack
   * @param attackId the id of the attack to delete
   */
  @DeleteMapping("/attack/{attackId}")
  void deleteOne(@PathVariable int attackId);

  /**
   * Update the notes of an attack
   * @param attackId the id of the attack to update
   * @param notes the new notes
   */
  @PatchMapping("/attack/{attackId}/notes")
  void updateNotesAttack(@PathVariable int attackId, @RequestBody String notes);

  /**
   * Update the id of server of an attack
   * @param attackId the id of the attack to update
   * @param serverId the new server id
   */
  @PatchMapping("/attack/{attackId}/server")
  void addServerToAttack(@PathVariable int attackId, @RequestBody int serverId);

  /**
   * Update the id of exploit of an attack
   * @param attackId the id of the attack to update
   * @param exploitId the new exploit id
   */
  @PatchMapping("/attack/{attackId}/exploit")
  void addExploitToAttack(@PathVariable int attackId, @RequestBody int exploitId);

  /**
   * Launch an attack by its id
   * @param attackId the id of the attack to update
   *
   */
  @PostMapping("/attack/{attackId}/launch")
  void launchAttack(@PathVariable int attackId);

  /**
   * Record the result of an attack by its id
   * @param attackId the id of the attack to update
   *
   */
  @PostMapping("/attack/{attackId}/result")
  void recordResult(@PathVariable int attackId, @RequestBody String result);
}
