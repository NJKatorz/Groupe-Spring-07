package be.vinci.ipl.projet2024.group07.gateway;

import be.vinci.ipl.projet2024.group07.gateway.models.Attack;
import be.vinci.ipl.projet2024.group07.gateway.models.Credentials;
import be.vinci.ipl.projet2024.group07.gateway.models.Exploit;
import be.vinci.ipl.projet2024.group07.gateway.models.Server;
import be.vinci.ipl.projet2024.group07.gateway.models.Target;
import be.vinci.ipl.projet2024.group07.gateway.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class GatewayController {

  private final GatewayService service;

  public GatewayController(GatewayService service) {
    this.service = service;
  }

  @GetMapping("/users")
  public ResponseEntity<User> readUserByEmail(@RequestParam(value = "email") String email) {
    return new ResponseEntity<>(service.readOneUserByEmail(email), HttpStatus.OK);
  }

  @PostMapping("/users")
  public ResponseEntity<Void> createUser(@RequestBody Credentials userWithCredentials) {
    service.createUser(userWithCredentials);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }


  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody Credentials credentials) {
    return new ResponseEntity<>(service.connect(credentials), HttpStatus.OK);
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<User> readUserByUserId(@PathVariable int userId) {
    return new ResponseEntity<>(service.readOneUserById(userId), HttpStatus.OK);
  }

  @DeleteMapping("/users/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable int userId,
      @RequestHeader("Authorization") String token) {
    service.verify(token);
    service.deleteUser(userId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/users/{userId}/name")
  public ResponseEntity<Void> updateUserName(@PathVariable int userId, @RequestBody String newName,
      @RequestHeader("Authorization") String token) {
    service.verify(token);
    service.updateUserName(userId, newName);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/users/{userId}/role")
  public ResponseEntity<Void> updateUserRole(@PathVariable int userId, @RequestBody String newRole,
      @RequestHeader("Authorization") String token) {
    service.verify(token);
    service.updateUserRole(userId, newRole);
    return new ResponseEntity<>(HttpStatus.OK);
  }

//  @PatchMapping("/users/{userId}/password")
//  public ResponseEntity<Void> updatePassword(@PathVariable int userId, @RequestBody Credentials userWithCredentials,
//      @RequestHeader("Authorization") String token) {
//    service.verify(token);
//    service.updateUserPassword(userId, userWithCredentials);
//    return new ResponseEntity<>(HttpStatus.OK);
//  }

  @GetMapping("/users/{userId}/exploits")
  public ResponseEntity<Iterable<Exploit>> readAllExploitsByUserId(@PathVariable int userId) {
    return new ResponseEntity<>(service.readAllUserExploits(userId), HttpStatus.OK);
  }

  @GetMapping("/targets")
  public ResponseEntity<Iterable<Target>> readAllTargets(
      @RequestParam(value = "minServers", required = false) int minServers,
      @RequestParam(value = "minRevenue", required = false) int minRevenue) {
    return new ResponseEntity<>(service.readAllTargets(minServers, minRevenue), HttpStatus.OK);
  }

  @PostMapping("/targets")
  public ResponseEntity<Void> createTarget(@RequestBody Target target) {
    service.createTarget(target);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/targets/{targetId}")
  public ResponseEntity<Target> readTarget(@PathVariable int targetId) {
    return new ResponseEntity<>(service.readOneTarget(targetId), HttpStatus.OK);
  }

  @PutMapping("/targets/{targetId}")
  public ResponseEntity<Void> updateTarget(@PathVariable int targetId, @RequestBody Target target) {
    if (target.getId() != targetId) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target id mismatch");
    }

    service.updateTarget(targetId, target);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/targets/{targetId}")
  public ResponseEntity<Void> deleteTarget(@PathVariable int targetId) {
    service.deleteTarget(targetId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/targets/{targetId}/servers")
  public ResponseEntity<Iterable<Server>> readAllServersByTargetId(@PathVariable int targetId) {
    return new ResponseEntity<>(service.readAllServersByTargetId(targetId), HttpStatus.OK);
  }

  @GetMapping("/targets/colocated")
  public ResponseEntity<Iterable<String>> readAllColocatedTargets() {
    return new ResponseEntity<>(service.readColocated(), HttpStatus.OK);
  }


  @PostMapping("servers")
  public ResponseEntity<Void> createServer(@RequestBody Server server) {
    service.createServer(server);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("servers/{serverId}")
  public ResponseEntity<Server> readServer(@PathVariable int serverId) {
    return new ResponseEntity<>(service.readOneServer(serverId), HttpStatus.OK);
  }

  @PutMapping("servers/{serverId}")
  public ResponseEntity<Void> updateServer(@PathVariable int serverId, @RequestBody Server server) {
    if (serverId != server.getId()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Server id mismatch");
    }
    service.updateServer(serverId, server);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("servers/{serverId}")
  public ResponseEntity<Void> deleteServer(@PathVariable int serverId) {
    service.deleteServer(serverId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/servers/{serverId}/validate")
  public ResponseEntity<Void> validateServer(@PathVariable int serverId) {
    service.validateServer(serverId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/exploits")
  public ResponseEntity<Iterable<Exploit>> readAllExploits(
      @RequestParam(value = "serverType", required = false) String serverType) {
    return new ResponseEntity<>(service.readAllExploits(serverType), HttpStatus.OK);
  }

  @PostMapping("/exploits")
  public ResponseEntity<Void> createExploit(@RequestBody Exploit exploit) {
    service.createExploit(exploit);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/exploits/{exploitId}")
  public ResponseEntity<Exploit> readOneExploit(@PathVariable int exploitId) {
    return new ResponseEntity<>(service.readOneExploit(exploitId), HttpStatus.OK);
  }

  @PutMapping("/exploits/{exploitId}")
  public ResponseEntity<Void> updateExploit(@PathVariable int exploitId,
      @RequestBody Exploit exploit) {
    if (exploit.getId() != exploitId) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target id mismatch");
    }
    service.updateExploit(exploitId, exploit);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/exploits/{exploitId}")
  public ResponseEntity<Void> deleteExploit(@PathVariable int exploitId) {
    service.deleteExploit(exploitId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/exploits/{exploitId}/validate")
  public ResponseEntity<Void> validateExploit(@PathVariable int exploitId) {
    service.validateExploit(exploitId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/attacks")
  public ResponseEntity<Iterable<Attack>> readAllAttacks() {
    return new ResponseEntity<>(service.readAllAttacks(), HttpStatus.OK);
  }

  @PostMapping("/attacks")
  public ResponseEntity<Void> createAttack(@RequestBody Attack attack) {
    service.createAttack(attack);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/attacks/{attackId}")
  public ResponseEntity<Attack> readOneAttack(@PathVariable int attackId) {
    return new ResponseEntity<>(service.readOneAttack(attackId), HttpStatus.OK);
  }

  @DeleteMapping("/attacks/{attackId}")
  public ResponseEntity<Void> deleteAttack(@PathVariable int attackId) {
    service.deleteAttack(attackId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/attacks/{attackId}/notes")
  public ResponseEntity<Void> updateNotesAttack(@PathVariable int attackId,
      @RequestBody String notes) {
    service.updateNotesAttack(attackId, notes);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/attacks/{attackId}/server")
  public ResponseEntity<Void> addServerToAttack(@PathVariable int attackId,
      @RequestBody int serverId) {
    service.addServerToAttack(attackId, serverId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping("/attacks/{attackId}/exploit")
  public ResponseEntity<Void> addExploitToAttack(@PathVariable int attackId,
      @RequestBody int exploitId) {
    service.addExploitToAttack(attackId, exploitId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/attacks/{attackId}/launch")
  public ResponseEntity<Void> launchAttack(@PathVariable int attackId) {
    service.lauchAttack(attackId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/attacks/{attackId}/result")
  public ResponseEntity<Void> recordResult(@PathVariable int attackId, @RequestBody String result) {
    service.recordResult(attackId, result);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
