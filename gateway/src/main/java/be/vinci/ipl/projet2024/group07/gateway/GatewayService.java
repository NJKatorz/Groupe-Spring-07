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

  public void createUser(Credentials userWithCredentials)
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

  public Iterable<Target> readAllTargets(int minServers, int minRevenue) {
    return targetsProxy.getAllTargets(minServers, minRevenue);
  }

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

  public Iterable<String> readColocated() {
    return targetsProxy.readColocated();
  }

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

  public Iterable<Exploit> readAllExploits(String serverType) {
    return exploitsProxy.readAll(serverType);
  }

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


  public Iterable<Attack> readAllAttacks() {
    return attacksProxy.readAll();
  }

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

