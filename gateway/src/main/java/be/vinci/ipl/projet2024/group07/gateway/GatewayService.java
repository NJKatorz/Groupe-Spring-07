package be.vinci.ipl.projet2024.group07.gateway;


import be.vinci.ipl.projet2024.group07.gateway.data.AttacksProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.AuthenticationProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.ServersProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.TargetsProxy;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.BadRequestException;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.ConflictException;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.NotFoundException;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.UnauthorizedException;
import be.vinci.ipl.projet2024.group07.gateway.models.Attack;
import be.vinci.ipl.projet2024.group07.gateway.models.Credentials;
import be.vinci.ipl.projet2024.group07.gateway.models.Server;
import be.vinci.ipl.projet2024.group07.gateway.models.Target;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


@Service
public class GatewayService {

  private AttacksProxy attacksProxy;
  private AuthenticationProxy authenticationProxy;
  private ServersProxy serversProxy;
  private TargetsProxy targetsProxy;

  public GatewayService(AttacksProxy attacksProxy, AuthenticationProxy authenticationProxy, ServersProxy serversProxy,
      TargetsProxy targetsProxy) {
    this.attacksProxy = attacksProxy;
    this.authenticationProxy = authenticationProxy;
    this.serversProxy = serversProxy;
    this.targetsProxy = targetsProxy;
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

  public String verify(String token) throws UnauthorizedException {
    try {
      return authenticationProxy.verify(token);
    } catch (FeignException e) {
      if (e.status() == 401) {
        throw new UnauthorizedException();
      }else{
        throw e;
      }
    }
  }


  public Iterable<Target> readAllTargets(int minServers, int minRevenue) {
    return targetsProxy.readAll(minServers, minRevenue);
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

  public Iterable<Target> readColocated() {
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

  public void lauchAttack(int attackId) throws NotFoundException {
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

