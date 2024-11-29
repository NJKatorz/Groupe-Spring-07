package be.vinci.ipl.projet2024.group07.gateway;


import be.vinci.ipl.projet2024.group07.gateway.data.AttacksProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.AuthenticationProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.ExploitsProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.ServersProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.TargetsProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.UsersProxy;
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
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


@Service
public class GatewayService {

  private AttacksProxy attacksProxy;
  private AuthenticationProxy authenticationProxy;
  private ExploitsProxy exploitsProxy;
  private ServersProxy serversProxy;
  private TargetsProxy targetsProxy;
  private UsersProxy usersProxy;

  public GatewayService(AttacksProxy attacksProxy, AuthenticationProxy authenticationProxy, ExploitsProxy exploitsProxy, ServersProxy serversProxy,
      TargetsProxy targetsProxy, UsersProxy usersProxy) {
    this.attacksProxy = attacksProxy;
    this.authenticationProxy = authenticationProxy;
    this.exploitsProxy = exploitsProxy;
    this.serversProxy = serversProxy;
    this.targetsProxy = targetsProxy;
    this.usersProxy = usersProxy;
  }

  /**
   * Connect a user
   * @param credentials the credentials of the user
   * @return the token
   */
  public String connect(Credentials credentials) throws UnauthorizedException, BadRequestException {
     try {
       return authenticationProxy.connect(credentials);
     } catch (FeignException e) {
       if (e.status() == 401) {
         throw new UnauthorizedException();
       } else if (e.status() == 400) {
         throw new BadRequestException();
       } else {
         throw e;
       }
     }
  }

  /**
   * Verify a token
   * @param token the token to verify
   * @return the user's email
   */
  public String verifyToken(String token) throws UnauthorizedException {
    try {
      return authenticationProxy.verifyToken(token);
    } catch (FeignException e) {
      if (e.status() == 401) {
        throw new UnauthorizedException();
      }else{
        throw e;
      }
    }
  }

  /**
   * Verify if the user is an admin
   * @param token the token to verify
   * @return true if the user is an admin
   */
  public boolean verifyUserIsAdmin(String token) throws UnauthorizedException {
    try {
      String authenticatedUserEmail = authenticationProxy.verifyToken(token);
      User user = usersProxy.getUserByEmail(authenticatedUserEmail);
      return user.getRole().equals("admin");
    } catch (FeignException e) {
      if (e.status() == 401) {
        throw new UnauthorizedException();
      }else{
        throw e;
      }
    }
  }

  /**
   * Retrieves a user by their email.
   *
   * @param email the email of the user
   * @return the user data
   * @throws NotFoundException if the user is not found
   */
  public User readOneUserByEmail(String email) throws NotFoundException {
    try {
      return usersProxy.getUserByEmail(email);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Creates a new user.
   *
   * @param userWithCredentials the user data and credentials
   * @throws BadRequestException if the user data is invalid
   * @throws ConflictException if the user already exists
   */
  public void createUser(UserWithCredentials userWithCredentials)
      throws BadRequestException, ConflictException {
    try {
      usersProxy.createOne(userWithCredentials);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new BadRequestException();
      } else if (e.status() == 409) {
        throw new ConflictException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Creates a new administrator user.
   *
   * @param userWithCredentials the admin user data and credentials
   * @throws BadRequestException if the user data is invalid
   * @throws ConflictException if the user already exists
   */
  // for the tests of the authentification
  public void createAdmin(UserWithCredentials userWithCredentials)
      throws BadRequestException, ConflictException {
    try {
      usersProxy.createAdmin(userWithCredentials);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new BadRequestException();
      } else if (e.status() == 409) {
        throw new ConflictException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Retrieves a user by their id.
   *
   * @param userId the id of the user
   * @return the user data
   * @throws NotFoundException if the user is not found
   */
  public User readOneUserById(int userId) throws NotFoundException {
    try {
      return usersProxy.readOneByUserId(userId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Deletes a user.
   *
   * @param userId the id of the user
   * @throws NotFoundException if the user is not found
   */
  public void deleteUser(int userId) throws NotFoundException {
    try {
      usersProxy.deleteUser(userId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Updates the name of a user.
   *
   * @param userId the id of the user
   * @param newName the new name
   * @throws BadRequestException if the new name is invalid
   * @throws NotFoundException if the user is not found
   */
  public void updateUserName(int userId, String newName)
      throws BadRequestException, NotFoundException {
    try {
      usersProxy.updateUserName(userId, newName);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new BadRequestException();
      } else if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Updates the role of a user.
   *
   * @param userId the id of the user
   * @param newRole the new role
   * @throws BadRequestException if the new role is invalid
   * @throws NotFoundException if the user is not found
   */
  public void updateUserRole(int userId, String newRole)
      throws BadRequestException, NotFoundException {
    try {
      usersProxy.updateUserRole(userId, newRole);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new BadRequestException();
      } else if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Updates the password of a user.
   *
   * @param userWithNewPassword the user data and new password
   * @throws BadRequestException if the new password is invalid
   * @throws NotFoundException if the user is not found
   */
  public void updateUserPassword(ChangePassword userWithNewPassword)
      throws BadRequestException, NotFoundException {
    try {
      authenticationProxy.changePassword(userWithNewPassword);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new BadRequestException();
      } else if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Retrieves all exploits created by a user.
   *
   * @param userId the id of the user
   * @return the exploits
   * @throws NotFoundException if the user is not found
   */
  Iterable<Exploit> readAllUserExploits(int userId) throws NotFoundException {
    try {
      return usersProxy.getExploitsByAuthor(userId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Retrieves all targets.
   *
   * @param minServers the minimum number of servers
   * @param minRevenue the minimum revenue
   * @return the targets
   */
  public Iterable<Target> readAllTargets(Integer minServers, Integer minRevenue) {
    return targetsProxy.getAllTargets(minServers, minRevenue);
  }

  /**
   * Creates a new target.
   *
   * @param target the target data
   * @throws BadRequestException if the target data is invalid
   * @throws ConflictException if the target already exists
   */
  public void createTarget(Target target) throws BadRequestException, ConflictException {
    try {
      targetsProxy.createOne(target);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new BadRequestException();
      } else if (e.status() == 409) {
        throw new ConflictException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Retrieves a target by its id.
   *
   * @param targetId the id of the target
   * @return the target data
   * @throws NotFoundException if the target is not found
   */
  public Target readOneTarget(int targetId) throws NotFoundException {
    try {
      return targetsProxy.readOne(targetId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Updates the data of a target.
   *
   * @param targetId the id of the target
   * @param target the new target data
   * @throws BadRequestException if the new target data is invalid
   * @throws NotFoundException if the target is not found
   */
  public void updateTarget(int targetId, Target target)
      throws BadRequestException, NotFoundException {
    try {
      targetsProxy.updateOne(targetId, target);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new BadRequestException();
      } else if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Deletes a target.
   *
   * @param targetId the id of the target
   * @throws NotFoundException if the target is not found
   */
  public void deleteTarget(int targetId) throws NotFoundException {
    try {
      targetsProxy.deleteOne(targetId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Retrieves all colocated targets.
   *
   * @return the colocated targets
   */
  public Iterable<String> readColocated() {
    return targetsProxy.readColocated();
  }

  /**
   * Creates a new server.
   *  @param server the server data
   * @throws BadRequestException if the server data is invalid
   * @throws ConflictException if the server already exists
   */
  public void createServer(Server server) throws BadRequestException, ConflictException {
    try {
      serversProxy.createOne(server);
    } catch (FeignException e) {
      if (e.status() == 409) {
        throw new ConflictException();
      } else if (e.status() == 409) {
        throw new BadRequestException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Retrieves a server by its id.
   *
   * @param serverId the id of the server
   * @return the server data
   * @throws NotFoundException if the server is not found
   */
  public Server readOneServer(int serverId) throws NotFoundException {
    try {
      return serversProxy.readOne(serverId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Updates the data of a server.
   *
   * @param serverId the id of the server
   * @param server the new server data
   * @throws BadRequestException if the new server data is invalid
   * @throws NotFoundException if the server is not found
   */
  public void updateServer(int serverId, Server server)
      throws BadRequestException, NotFoundException {
    try {
      serversProxy.updateOne(serverId, server);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new BadRequestException();
      } else if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Deletes a server.
   *
   * @param serverId the id of the server
   * @throws NotFoundException if the server is not found
   */
  public void deleteServer(int serverId) throws NotFoundException {
    try {
      serversProxy.deleteOne(serverId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Validates a server.
   *
   * @param serverId the id of the server
   * @throws NotFoundException if the server is not found
   */
  public void validateServer(int serverId) throws NotFoundException {
    try {
      serversProxy.validateServer(serverId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Retrieves all servers of a target.
   *
   * @param targetId the id of the target
   * @return the servers
   * @throws NotFoundException if the target is not found
   */
  public Iterable<Server> readAllServersByTargetId(@PathVariable int targetId)
      throws NotFoundException {
    try {
      return serversProxy.readAllServersByTargetId(targetId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Retrieves all exploits.
   *
   * @param serverType the type of server to filter
   * @return the exploits
   */
  public Iterable<Exploit> readAllExploits(String serverType) {
    return exploitsProxy.readAll(serverType);
  }

  /**
   * Creates a new exploit.
   *
   * @param exploit the exploit data
   * @throws BadRequestException if the exploit data is invalid
   * @throws ConflictException if the exploit already exists
   */
  public void createExploit(Exploit exploit) throws BadRequestException, ConflictException {
    try {
      exploitsProxy.createOne(exploit);
    } catch (FeignException e) {
      if (e.status() == 409) {
        throw new ConflictException();
      } else if (e.status() == 400) {
        throw new BadRequestException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Retrieves an exploit by its id.
   *
   * @param exploitId the id of the exploit
   * @return the exploit data
   * @throws NotFoundException if the exploit is not found
   */
  public Exploit readOneExploit(int exploitId) throws NotFoundException {
    try {
      return exploitsProxy.readOne(exploitId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Updates the data of an exploit.
   *
   * @param exploitId the id of the exploit
   * @param exploit the new exploit data
   * @throws BadRequestException if the new exploit data is invalid
   * @throws NotFoundException if the exploit is not found
   */
  public void updateExploit(int exploitId, Exploit exploit)
      throws BadRequestException, NotFoundException {
    try {
      exploitsProxy.updateOne(exploitId, exploit);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new BadRequestException();
      } else if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Deletes an exploit.
   *
   * @param exploitId the id of the exploit
   * @throws NotFoundException if the exploit is not found
   */
  public void deleteExploit(int exploitId) throws NotFoundException {
    try {
      exploitsProxy.deleteOne(exploitId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Validates an exploit.
   *
   * @param exploitId the id of the exploit
   * @throws NotFoundException if the exploit is not found
   */
  public void validateExploit(int exploitId) throws NotFoundException {
    try {
      exploitsProxy.validateExploit(exploitId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Retrieves all attacks.
   *
   * @return the attacks
   */
  public Iterable<Attack> readAllAttacks() {
    return attacksProxy.readAll();
  }

  /**
   * Creates a new attack.
   *
   * @param attack the attack data
   * @throws BadRequestException if the attack data is invalid
   * @throws ConflictException if the attack already exists
   */
  public void createAttack(Attack attack) throws BadRequestException, ConflictException {
    try {
      attacksProxy.createOne(attack);
    } catch (FeignException e) {
      if (e.status() == 409) {
        throw new ConflictException();
      } else if (e.status() == 400) {
        throw new BadRequestException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Retrieves an attack by its id.
   *
   * @param attackId the id of the attack
   * @return the attack data
   * @throws NotFoundException if the attack is not found
   */
  public Attack readOneAttack(int attackId) throws NotFoundException {
    try {
      return attacksProxy.readOne(attackId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Deletes an attack.
   *
   * @param attackId the id of the attack
   * @throws NotFoundException if the attack is not found
   */
  public void deleteAttack(int attackId) throws NotFoundException {
    try {
      attacksProxy.deleteOne(attackId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Updates the notes of an attack.
   *
   * @param attackId the id of the attack
   * @param notes the new notes
   * @throws BadRequestException if the new notes are invalid
   * @throws NotFoundException if the attack is not found
   */
  public void updateNotesAttack(int attackId, String notes)
      throws NotFoundException, BadRequestException {
    try {
      attacksProxy.updateNotesAttack(attackId, notes);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new BadRequestException();
      } else if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Adds a server to an attack.
   *
   * @param attackId the id of the attack
   * @param serverId the id of the server
   * @throws NotFoundException if the attack or the server is not found
   * @throws BadRequestException if the server is invalid
   */
  public void addServerToAttack(int attackId, int serverId)
      throws NotFoundException, BadRequestException {
    try {
      attacksProxy.addServerToAttack(attackId, serverId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else if (e.status() == 400) {
        throw new BadRequestException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Adds an exploit to an attack.
   *
   * @param attackId the id of the attack
   * @param exploitId the id of the exploit
   * @throws NotFoundException if the attack or the exploit is not found
   * @throws BadRequestException if the exploit is invalid
   */
  public void addExploitToAttack(int attackId, int exploitId)
      throws NotFoundException, BadRequestException {
    try {
      attacksProxy.addExploitToAttack(attackId, exploitId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else if (e.status() == 400) {
        throw new BadRequestException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Launches an attack.
   *
   * @param attackId the id of the attack
   * @throws NotFoundException if the attack is not found
   */
  public void launchAttack(int attackId) throws NotFoundException {
    try {
      attacksProxy.launchAttack(attackId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Records the result of an attack.
   *
   * @param attackId the id of the attack
   * @param result the result
   * @throws NotFoundException if the attack is not found
   * @throws BadRequestException if the result is invalid
   */
  public void recordResult(int attackId, String result)
      throws NotFoundException, BadRequestException {
    try {
      attacksProxy.recordResult(attackId, result);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else if (e.status() == 400) {
        throw new BadRequestException();
      } else {
        throw e;
      }
    }
  }

}

