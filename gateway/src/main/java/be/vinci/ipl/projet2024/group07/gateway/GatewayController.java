package be.vinci.ipl.projet2024.group07.gateway;

import be.vinci.ipl.projet2024.group07.gateway.exceptions.BadRequestException;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.ConflictException;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.NotFoundException;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.UnauthorizedException;
import be.vinci.ipl.projet2024.group07.gateway.models.Attack;
import be.vinci.ipl.projet2024.group07.gateway.models.ChangePassword;
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
  public ResponseEntity<User> readUserByEmail(@RequestParam(value = "email") String email,
      @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      return new ResponseEntity<>(service.readOneUserByEmail(email), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/users")
  public ResponseEntity<Void> createUser(@RequestBody Credentials userWithCredentials) {
    try {
      service.createUser(userWithCredentials);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    } catch (ConflictException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }
  }


  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody Credentials credentials) {
    try {
      return new ResponseEntity<>(service.connect(credentials), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<User> readUserByUserId(@PathVariable int userId,
      @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      return new ResponseEntity<>(service.readOneUserById(userId), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/users/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable int userId,
      @RequestHeader("Authorization") String token) {
    try {

      String authenticatedUserEmail = service.verifyToken(token);
      User authenticatedUser = service.readOneUserByEmail(authenticatedUserEmail);

      if (service.verifyUserIsAdmin(token)) {
        service.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      if (authenticatedUser.getId() == userId) {
        service.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } else {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
      }

    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @PatchMapping("/users/{userId}/name")
  public ResponseEntity<Void> updateUserName(@PathVariable int userId, @RequestBody String newName,
      @RequestHeader("Authorization") String token) {
    try {

      String authenticatedUserEmail = service.verifyToken(token);
      User authenticatedUser = service.readOneUserByEmail(authenticatedUserEmail);

      if (service.verifyUserIsAdmin(token)) {
        service.updateUserName(userId, newName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      if (authenticatedUser.getId() == userId) {
        service.updateUserName(userId, newName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } else {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
      }

    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping("/users/{userId}/role")
  public ResponseEntity<Void> updateUserRole(@PathVariable int userId, @RequestBody String newRole,
      @RequestHeader("Authorization") String token) {
    try {

      if (service.verifyUserIsAdmin(token)) {
        service.updateUserRole(userId, newRole);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } else {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
      }

    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping("/users/{userId}/password")
  public ResponseEntity<Void> changePassword(@PathVariable int userId,
      @RequestBody ChangePassword userWithNewPassword,
      @RequestHeader("Authorization") String token) {
    try {
      String authenticatedUserEmail = service.verifyToken(token);
      User authenticatedUser = service.readOneUserByEmail(authenticatedUserEmail);

      if (service.verifyUserIsAdmin(token)) {
        service.updateUserPassword(userWithNewPassword);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      if (authenticatedUser.getId() == userId) {
        service.updateUserPassword(userWithNewPassword);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } else {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
      }

    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/users/{userId}/exploits")
  public ResponseEntity<Iterable<Exploit>> readAllExploitsByUserId(@PathVariable int userId,
      @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      return new ResponseEntity<>(service.readAllUserExploits(userId), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }

  @GetMapping("/targets")
  public ResponseEntity<Iterable<Target>> readAllTargets(
      @RequestParam(value = "minServers", required = false) int minServers,
      @RequestParam(value = "minRevenue", required = false) int minRevenue,
      @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      return new ResponseEntity<>(service.readAllTargets(minServers, minRevenue), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/targets")
  public ResponseEntity<Void> createTarget(@RequestBody Target
      target, @RequestHeader("Authorization") String token) {
    try {
      if (!service.verifyUserIsAdmin(token)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
      }
      service.createTarget(target);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/targets/{targetId}")
  public ResponseEntity<Target> readTarget(@PathVariable int targetId) {
    try {
      return new ResponseEntity<>(service.readOneTarget(targetId), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/targets/{targetId}")
  public ResponseEntity<Void> updateTarget(@PathVariable int targetId,
      @RequestBody Target target, @RequestHeader("Authorization") String token) {
    try {
      if (!service.verifyUserIsAdmin(token)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
      }

      if (target.getId() != targetId) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
      }

      service.updateTarget(targetId, target);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/targets/{targetId}")
  public ResponseEntity<Void> deleteTarget(@PathVariable int targetId,
      @RequestHeader("Authorization") String token) {
    try {
      if (service.verifyUserIsAdmin(token)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
      }
      service.deleteTarget(targetId);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/targets/{targetId}/servers")
  public ResponseEntity<Iterable<Server>> readAllServersByTargetId(@PathVariable int targetId,
      @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      return new ResponseEntity<>(service.readAllServersByTargetId(targetId), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }

  @GetMapping("/targets/colocated")
  public ResponseEntity<Iterable<String>> readAllColocatedTargets
      (@RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      return new ResponseEntity<>(service.readColocated(), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("servers")
  public ResponseEntity<Void> createServer(@RequestBody Server server,
      @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      service.createServer(server);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("servers/{serverId}")
  public ResponseEntity<Server> readServer(@PathVariable int serverId,
      @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      return new ResponseEntity<>(service.readOneServer(serverId), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("servers/{serverId}")
  public ResponseEntity<Void> updateServer(@PathVariable int serverId,
      @RequestBody Server server, @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      if (server.getId() != serverId) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
      }
      service.updateServer(serverId, server);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("servers/{serverId}")
  public ResponseEntity<Void> deleteServer(@PathVariable int serverId,
      @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      service.deleteServer(serverId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @PatchMapping("/servers/{serverId}/validate")
  public ResponseEntity<Void> validateServer(@PathVariable int serverId,
      @RequestHeader("Authorization") String token) {
    try {
      if (service.verifyUserIsAdmin(token)) {
        service.validateServer(serverId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } else {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
      }
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/exploits")
  public ResponseEntity<Iterable<Exploit>> readAllExploits(
      @RequestParam(value = "serverType", required = false) String serverType,
      @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      return new ResponseEntity<>(service.readAllExploits(serverType), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/exploits")
  public ResponseEntity<Void> createExploit(@RequestBody Exploit exploit, @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      service.createExploit(exploit);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/exploits/{exploitId}")
  public ResponseEntity<Exploit> readOneExploit(@PathVariable int exploitId, @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      return new ResponseEntity<>(service.readOneExploit(exploitId), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/exploits/{exploitId}")
  public ResponseEntity<Void> updateExploit(@PathVariable int exploitId,
      @RequestBody Exploit exploit, @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      if (exploit.getId() != exploitId) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
      }
      service.updateExploit(exploitId, exploit);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/exploits/{exploitId}")
  public ResponseEntity<Void> deleteExploit(@PathVariable int exploitId, @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      service.deleteExploit(exploitId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @PatchMapping("/exploits/{exploitId}/validate")
  public ResponseEntity<Void> validateExploit(@PathVariable int exploitId, @RequestHeader("Authorization") String token) {
    try {
      if (service.verifyUserIsAdmin(token)) {
        service.validateExploit(exploitId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } else {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
      }
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/attacks")
  public ResponseEntity<Iterable<Attack>> readAllAttacks(@RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      return new ResponseEntity<>(service.readAllAttacks(), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/attacks")
  public ResponseEntity<Void> createAttack(@RequestBody Attack attack, @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      service.createAttack(attack);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/attacks/{attackId}")
  public ResponseEntity<Attack> readOneAttack(@PathVariable int attackId, @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      return new ResponseEntity<>(service.readOneAttack(attackId), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/attacks/{attackId}")
  public ResponseEntity<Void> deleteAttack(@PathVariable int attackId, @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      service.deleteAttack(attackId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @PatchMapping("/attacks/{attackId}/notes")
  public ResponseEntity<Void> updateNotesAttack(@PathVariable int attackId,
      @RequestBody String notes, @RequestHeader("Authorization") String token) {
   try {
      service.verifyToken(token);
      service.updateNotesAttack(attackId, notes);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping("/attacks/{attackId}/server")
  public ResponseEntity<Void> addServerToAttack(@PathVariable int attackId,
      @RequestBody int serverId, @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      service.addServerToAttack(attackId, serverId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @PatchMapping("/attacks/{attackId}/exploit")
  public ResponseEntity<Void> addExploitToAttack(@PathVariable int attackId,
      @RequestBody int exploitId, @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      service.addExploitToAttack(attackId, exploitId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/attacks/{attackId}/launch")
  public ResponseEntity<Void> launchAttack(@PathVariable int attackId, @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      if (!service.verifyUserIsAdmin(token)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
      }
      service.launchAttack(attackId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

  }

  @PostMapping("/attacks/{attackId}/result")
  public ResponseEntity<Void> recordResult(@PathVariable int attackId,
      @RequestBody String result, @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      if (!service.verifyUserIsAdmin(token)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
      }
      service.recordResult(attackId, result);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
  }
}
