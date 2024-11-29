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
import be.vinci.ipl.projet2024.group07.gateway.models.UserWithCredentials;
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

  /**
   * Retrieves a user by email.
   *
   * @param email the email of the user to retrieve
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the user data and HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the user is not found, or the request is invalid
   */
  @GetMapping("/users")
  public ResponseEntity<User> readUserByEmail(@RequestParam String email,
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

  /**
   * Creates a new user.
   *
   * @param userWithCredentials the user data to create
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the user already exists, or the request is invalid
   */
  @PostMapping("/users")
  public ResponseEntity<Void> createUser(@RequestBody UserWithCredentials userWithCredentials) {
    try {
      service.createUser(userWithCredentials);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user data");
    } catch (ConflictException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
    }
  }

  /**
   * Logs in a user.
   *
   * @param credentials the user's credentials
   * @return ResponseEntity containing the token and HTTP status
   * @throws ResponseStatusException if the request is unauthorized or the request is invalid
   */
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

  /**
   * Retrieves a user by ID.
   *
   * @param userId the ID of the user to retrieve
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the user data and HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the user is not found, or the request is invalid
   */
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

  /**
   * Deletes a user.
   *
   * @param userId the ID of the user to delete
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the user is not found, or the request is invalid
   */
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

  /**
   * Updates a user's name.
   *
   * @param userId the ID of the user to update
   * @param newName the new name
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the user is not found, the request is invalid, or the user is not allowed to update the name
   */
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

  /**
   * Updates a user's role.
   *
   * @param userId the ID of the user to update
   * @param newRole the new role
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the user is not found, the request is invalid, or the user is not allowed to update the role
   */
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

  /**
   * Updates a user's password.
   *
   * @param userId the ID of the user to update
   * @param userWithNewPassword the user's email and the new password
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the user is not found, the request is invalid, or the user is not allowed to update the password
   */
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

  /**
   * Retrieves all exploits by author.
   *
   * @param authorId the ID of the author
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the exploits and HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the user is not found, or the request is invalid
   */
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

  /**
   * Retrieves all targets.
   *
   * @param minServers the minimum number of servers
   * @param minRevenue the minimum revenue
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the targets and HTTP status
   * @throws ResponseStatusException if the request is unauthorized or the request is invalid
   */
  @GetMapping("/targets")
  public ResponseEntity<Iterable<Target>> readAllTargets(
      @RequestParam(value = "minServers", required = false) Integer minServers,
      @RequestParam(value = "minRevenue", required = false) Integer minRevenue,
      @RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      return new ResponseEntity<>(service.readAllTargets(minServers, minRevenue), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }

  /**
   * Creates a new target.
   *
   * @param target the target data to create
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the user is not an admin, or the request is invalid
   */
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

  /**
   * Retrieves a target by ID.
   *
   * @param targetId the ID of the target to retrieve
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the target data and HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the target is not found, or the request is invalid
   */
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

  /**
   * Updates a target.
   *
   * @param targetId the ID of the target to update
   * @param target the new target data
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the user is not an admin, the target is not found, or the request is invalid
   */
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

  /**
   * Deletes a target.
   *
   * @param targetId the ID of the target to delete
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the user is not an admin, the target is not found, or the request is invalid
   */
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

  /**
   * Retrieves all servers by target ID.
   *
   * @param targetId the ID of the target
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the servers and HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the target is not found, or the request is invalid
   */
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

  /**
   * Retrieves all colocated targets.
   *
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the colocated targets and HTTP status
   * @throws ResponseStatusException if the request is unauthorized
   */
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

  /**
   * Retrieves all servers.
   *
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the servers and HTTP status
   * @throws ResponseStatusException if the request is unauthorized
   */
  @PostMapping("/servers")
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

  /**
   * Creates a new server.
   *
   * @param server the server data to create
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized or the request is invalid
   */
  @GetMapping("/servers/{serverId}")
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

  /**
   * Updates a server.
   *
   * @param serverId the ID of the server to update
   * @param server the new server data
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the server is not found, or the request is invalid
   */
  @PutMapping("/servers/{serverId}")
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

  /**
   * Deletes a server.
   *
   * @param serverId the ID of the server to delete
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the server is not found, or the request is invalid
   */
  @DeleteMapping("/servers/{serverId}")
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

  /**
   * Validates a server.
   *
   * @param serverId the ID of the server to validate
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the server is not found, or the request is invalid
   */
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

  /**
   * Retrieves all exploits.
   *
   * @param serverType the type of server to filter
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the exploits and HTTP status
   * @throws ResponseStatusException if the request is unauthorized or the request is invalid
   */
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

  /**
   * Creates a new exploit.
   *
   * @param exploit the exploit data to create
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the request is invalid, or the user is not an admin
   */
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

  /**
   * Retrieves an exploit by ID.
   *
   * @param exploitId the ID of the exploit to retrieve
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the exploit data and HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the exploit is not found, or the request is invalid
   */
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

  /**
   * Updates an exploit.
   *
   * @param exploitId the ID of the exploit to update
   * @param exploit the new exploit data
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the exploit is not found, or the request is invalid
   */
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

  /**
   * Deletes an exploit.
   *
   * @param exploitId the ID of the exploit to delete
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the exploit is not found, or the request is invalid
   */
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

  /**
   * Validates an exploit.
   *
   * @param exploitId the ID of the exploit to validate
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the exploit is not found, or the request is invalid
   */
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

  /**
   * Retrieves all attacks.
   *
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the attacks and HTTP status
   * @throws ResponseStatusException if the request is unauthorized
   */
  @GetMapping("/attacks")
  public ResponseEntity<Iterable<Attack>> readAllAttacks(@RequestHeader("Authorization") String token) {
    try {
      service.verifyToken(token);
      return new ResponseEntity<>(service.readAllAttacks(), HttpStatus.OK);
    } catch (UnauthorizedException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
  }

  /**
   * Creates a new attack.
   *
   * @param attack the attack data to create
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the request is invalid, or the user is not an admin
   */
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

  /**
   * Retrieves an attack by ID.
   *
   * @param attackId the ID of the attack to retrieve
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the attack data and HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the attack is not found, or the request is invalid
   */
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

  /**
   * Deletes an attack.
   *
   * @param attackId the ID of the attack to delete
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the attack is not found, or the request is invalid
   */
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

  /**
   * Updates the notes of an attack.
   *
   * @param attackId the ID of the attack to update
   * @param notes the new notes
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the attack is not found, or the request is invalid
   */
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

  /**
   * Adds a server to an attack.
   *
   * @param attackId the ID of the attack
   * @param serverId the ID of the server to add
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the attack or server is not found, or the request is invalid
   */
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

  /**
   * Adds an exploit to an attack.
   *
   * @param attackId the ID of the attack
   * @param exploitId the ID of the exploit to add
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the attack or exploit is not found, or the request is invalid
   */
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

  /**
   * Launches an attack.
   *
   * @param attackId the ID of the attack to launch
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the attack is not found, or the request is invalid
   */
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

  /**
   * Records the result of an attack.
   *
   * @param attackId the ID of the attack
   * @param result the result of the attack
   * @param token the authorization token to verify the user's access
   * @return ResponseEntity containing the HTTP status
   * @throws ResponseStatusException if the request is unauthorized, the attack is not found, or the request is invalid
   */
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

  /**
   * Creates a new administrator user.
   *
   * @param userWithCredentials the user information and credentials to create
   * @return ResponseEntity with HTTP status CREATED if the administrator user is successfully created
   * @throws ResponseStatusException with HTTP status BAD_REQUEST if the provided user data is invalid
   * @throws ResponseStatusException with HTTP status CONFLICT if a user with the same credentials already exists
   */
  // for the tests of the authentification
  @PostMapping("/users/admin")
  public ResponseEntity<Void> createAdmin(@RequestBody UserWithCredentials userWithCredentials) {
    try {
      service.createAdmin(userWithCredentials);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (BadRequestException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user data");
    } catch (ConflictException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
    }
  }
}
