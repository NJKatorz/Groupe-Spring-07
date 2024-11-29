package be.vinci.ipl.projet2024.group07.servers;

import be.vinci.ipl.projet2024.group07.servers.models.Server;
import be.vinci.ipl.projet2024.group07.servers.models.Target;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController

public class ServerController {

  private final ServerService serverService;

  public ServerController(ServerService serverService) {
    this.serverService = serverService;
  }

  @GetMapping("/servers/{serverId}")
  public ResponseEntity<Server> readOne(@PathVariable int serverId) {
    Server server = serverService.getOne(serverId);
    if (server == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(server, HttpStatus.OK);
  }

  @PostMapping("/servers")
  public ResponseEntity<Server> createOne(@RequestBody Server newServer) {
    if (newServer.getIpAdress()==null || newServer.getServerType()==null || newServer.getTechnology()==null
        || newServer.getTargetId() == 0) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    newServer.setValidated(false);
    Server server = serverService.createOne(newServer);
    if (server == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(server, HttpStatus.CREATED);
  }

  @PutMapping("/servers/{serverId}")
  public ResponseEntity<Void> updateOne(@PathVariable int serverId, @RequestBody Server updatedServer) {
    if (updatedServer.getIpAdress()==null || updatedServer.getServerType()==null || updatedServer.getTechnology()==null
       || serverId != updatedServer.getId() ) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    Server server = serverService.getOne(serverId);
    if (server == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    if (server.getTargetId() !=  updatedServer.getTargetId() || server.isValidated() != updatedServer.isValidated()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    serverService.updateOne(updatedServer);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/servers/{serverId}")
  public ResponseEntity<Void> deleteOne(@PathVariable int serverId) {
    Server server =serverService.deleteOne(serverId);
    if (server == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PatchMapping("/servers/{serverId}/validate")
  public ResponseEntity<Void> validateServer(@PathVariable int serverId) {
    Server server = serverService.getOne(serverId);
    if (server == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    if (server.isValidated()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    serverService.validateServer(server);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/servers/target/{targetId}")
  public ResponseEntity<Iterable<Server>> readByTarget(@PathVariable int targetId) {
    Iterable<Server> servers = serverService.getByTarget(targetId);
    return new ResponseEntity<>(servers, HttpStatus.OK);
  }

  @DeleteMapping("/servers/target/{targetId}")
  public ResponseEntity<Void> deleteByTarget(@PathVariable int targetId) {
    serverService.deleteByTarget(targetId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
