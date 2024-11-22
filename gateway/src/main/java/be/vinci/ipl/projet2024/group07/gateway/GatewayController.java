package be.vinci.ipl.projet2024.group07.gateway;

import be.vinci.ipl.projet2024.group07.gateway.models.Exploit;
import be.vinci.ipl.projet2024.group07.gateway.models.Server;
import be.vinci.ipl.projet2024.group07.gateway.models.Target;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class GatewayController {
  private final GatewayService service;

  public GatewayController(GatewayService service) {
    this.service = service;
  }

  @GetMapping("/exploits")
  public Iterable<Exploit> readAllExploits(@RequestParam(value = "serverType", required = false) String serverType) {
    return service.readAllExploits(serverType);
  }

  @PostMapping("/exploits")
  public ResponseEntity<Void> createExploit(@RequestBody Exploit exploit) {
    service.createExploit(exploit);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/exploits/{exploitId}")
  public Exploit readOneExploit(@PathVariable int exploitId) {
    return service.readOneExploit(exploitId);
  }

  @PutMapping("/exploits/{exploitId}")
  public void updateExploit(@PathVariable int exploitId, @RequestBody Exploit exploit) {
    if (exploit.getId() != exploitId){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target id mismatch");
    }
    service.updateExploit(exploitId, exploit);
  }

  @DeleteMapping("/exploits/{exploitId}")
  public void deleteExploit(@PathVariable int exploitId) {
    service.deleteExploit(exploitId);
  }

  @PatchMapping("/exploits/{exploitId}/validate")
  public void validateExploit(@PathVariable int exploitId) {
    service.validateExploit(exploitId);
  }

  @PostMapping("servers")
  public ResponseEntity<Void> createServer(@RequestBody Server server){
    service.createServer(server);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("servers/{serverId}")
  public Server readServer(@PathVariable int serverId){
    return service.readOneServer(serverId);
  }

  @PutMapping("servers/{serverId}")
  public void updateServer(@PathVariable int serverId, @RequestBody Server server) {
    if (serverId != server.getId())
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Server id mismatch");
    service.updateServer(serverId, server);
  }

  @DeleteMapping("servers/{serverId}")
  public void deleteServer(@PathVariable int serverId) {
    service.deleteServer(serverId);
  }

  @PatchMapping("/servers/{serverId}/validate")
  public void validateServer(@PathVariable int serverId) {
    service.validateServer(serverId);
  }


  @GetMapping("/targets")
  public Iterable<Target> readAllTargets(){
    return service.readAllTargets();
  }

  @PostMapping("/targets")
  public ResponseEntity<Void> createTarget(@RequestBody Target target){
    service.createTarget(target);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/targets/{targetId}")
  public Target readTarget(@PathVariable int targetId){
    return service.readOneTarget(targetId);
  }

  @PutMapping("/targets/{targetId}")
  public void updateTarget(@PathVariable int targetId, @RequestBody Target target) {
    if (target.getId() != targetId){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target id mismatch");
    }
    service.updateTarget(targetId, target);
  }

  @DeleteMapping("/targets/{targetId}")
  public void deleteTarget(@PathVariable int targetId) {
    service.deleteTarget(targetId);
  }

  @GetMapping("/targets/colocated")
  public Iterable<Target> readAllColocatedTargets(){
    return service.readColocated();
  }
}
