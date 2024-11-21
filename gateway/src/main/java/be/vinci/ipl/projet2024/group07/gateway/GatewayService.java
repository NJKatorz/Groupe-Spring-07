package be.vinci.ipl.projet2024.group07.gateway;


import be.vinci.ipl.projet2024.group07.gateway.data.ServersProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.TargetsProxy;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.BadRequestException;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.ConflictException;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.NotFoundException;
import be.vinci.ipl.projet2024.group07.gateway.models.Server;
import be.vinci.ipl.projet2024.group07.gateway.models.Target;
import feign.FeignException;
import org.springframework.stereotype.Service;


@Service
public class GatewayService {

  private ServersProxy serversProxy;
  private TargetsProxy targetsProxy;


  public GatewayService(ServersProxy serversProxy, TargetsProxy targetsProxy) {
    this.serversProxy = serversProxy;
    this.targetsProxy = targetsProxy;
  }


  public Iterable<Target> readAllTargets() {
    return targetsProxy.readAll();
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

  public Target readTarget(int targetId) throws NotFoundException {
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
    return targetsProxy.readAll();
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

  public Server readServer(int serverId) throws NotFoundException {
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

  public Iterable<Server> readAllServersByTarget(int targetId) throws NotFoundException {
    try {
      return serversProxy.readByTarget(targetId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        throw e;
      }
    }
  }

  public void deleteAllServersByTarget(int targetId) throws NotFoundException {
    try {
      serversProxy.deleteByTarget(targetId);
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      }
    }
  }

}

