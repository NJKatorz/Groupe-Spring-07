package be.vinci.ipl.projet2024.group07.mockservers;

import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ServersController {

  @GetMapping("/servers/{serverId}")
  public Server readOne(@PathVariable int serverId) {
    Server server = new Server();
    server.setId(serverId);
    server.setIpAddress("192.168.1.1");
    server.setTargetId(54321);
    server.setServerType("Web");
    server.setTechnology("Apache");
    server.setValidated(true);
    return server;
  }

  @PostMapping("/servers")
  public ResponseEntity<Server> createOne(@RequestBody Server newServer) {
    return new ResponseEntity<>(newServer, HttpStatus.CREATED);
  }

  @PutMapping("/servers/{serverId}")
  public ResponseEntity<Void> updateOne(@PathVariable int serverId, @RequestBody Server updatedServer) {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/servers/{serverId}")
  public ResponseEntity<Void> deleteOne(@PathVariable int serverId) {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PatchMapping("/servers/{serverId}/validate")
  public ResponseEntity<Void> validateServer(@PathVariable int serverId) {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/servers/target/{targetId}")
  public ResponseEntity<Iterable<Server>> readByTarget(@PathVariable int targetId) {
    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
  }

  @DeleteMapping("/servers/target/{targetId}")
  public ResponseEntity<Void> deleteByTarget(@PathVariable int targetId) {
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
