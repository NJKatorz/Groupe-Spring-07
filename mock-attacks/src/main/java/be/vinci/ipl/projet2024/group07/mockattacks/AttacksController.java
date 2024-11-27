package be.vinci.ipl.projet2024.group07.mockattacks;

import be.vinci.ipl.projet2024.group07.mockattacks.Attack.Status;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AttacksController {

  @GetMapping("/attacks")
  public Iterable<Attack> readAll() {
    return new ArrayList<>();
  }

  @PostMapping("/attacks")
  public ResponseEntity<Attack> createOne(@RequestBody Attack newAttack) {
    return new ResponseEntity<>(newAttack, HttpStatus.CREATED);
  }

  @GetMapping("/attacks/{attackId}")
  public ResponseEntity<Attack> readOne(@PathVariable int attackId) {
    Attack attack = new Attack();
    attack.setId(attackId);
    attack.setTargetId(54321);
    attack.setServerId(12345);
    attack.setExploitId(67890);
    attack.setStatus(Status.PlANIFIEE);
    attack.setNotes("Ceci est une note");
    return new ResponseEntity<>(attack, HttpStatus.OK);
  }

  @DeleteMapping("/attacks/{attackId}")
  public ResponseEntity<Void> deleteOne(@PathVariable int attackId) {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PatchMapping("/attacks/{attackId}/server")
  public ResponseEntity<Void> updateServer(@PathVariable int attackId, @RequestBody Attack updatedAttack) {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PatchMapping("/attacks/{attackId}/notes")
  public ResponseEntity<Void> updateNotes(@PathVariable int attackId, @RequestBody Attack updatedAttack) {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PatchMapping("/attacks/{attackId}/exploit")
  public ResponseEntity<Void> updateExploit(@PathVariable int attackId, @RequestBody Attack updatedAttack) {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/attacks/{attackId}/launch")
  public ResponseEntity<Void> launchAttack(@PathVariable int attackId) {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/attacks/{attackId}/result")
  public ResponseEntity<Void> recordResult(@PathVariable int attackId, @RequestBody String result) {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/attacks/target/{targetId}")
  public ResponseEntity<Void> deleteByTarget(@PathVariable int targetId) {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/attacks/server/{serverId}")
  public ResponseEntity<Void> deleteByServer(@PathVariable int serverId) {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/attacks/exploit/{exploitId}")
  public ResponseEntity<Void> deleteByExploit(@PathVariable int exploitId) {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}