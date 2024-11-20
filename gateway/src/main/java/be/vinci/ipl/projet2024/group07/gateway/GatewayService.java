package be.vinci.ipl.projet2024.group07.gateway;

import be.vinci.ipl.projet2024.group07.gateway.data.AttacksProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.AuthenticationProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.ExploitsProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.ServersProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.TargetsProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.UsersProxy;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.BadRequestException;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.ConflictException;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.ForbiddenException;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.NotFoundException;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.UnauthorizedException;
import be.vinci.ipl.projet2024.group07.gateway.models.Credentials;
import be.vinci.ipl.projet2024.group07.gateway.models.Target;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class GatewayService {

  private final AttacksProxy attacksProxy;
  private final AuthenticationProxy authenticationProxy;
  private final ExploitsProxy exploitsProxy;
  private final ServersProxy serversProxy;
  private final TargetsProxy targetsProxy;
  private final UsersProxy usersProxy;

  public GatewayService(AttacksProxy attacksProxy, AuthenticationProxy authenticationProxy,
      ExploitsProxy exploitsProxy, ServersProxy serversProxy, TargetsProxy targetsProxy,
      UsersProxy usersProxy) {
    this.attacksProxy = attacksProxy;
    this.authenticationProxy = authenticationProxy;
    this.exploitsProxy = exploitsProxy;
    this.serversProxy = serversProxy;
    this.targetsProxy = targetsProxy;
    this.usersProxy = usersProxy;
  }

  public String connect(Credentials credentials) throws BadRequestException, UnauthorizedException {
    try {
      return authenticationProxy.connect(credentials);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new BadRequestException();
      } else if (e.status() == 401) {
        throw new UnauthorizedException();
      } else {
        throw e;
      }
    }
  }

  public void verify(String token, String expectedUserEmail)
      throws ForbiddenException, UnauthorizedException {
    try {
      String userEmail = authenticationProxy.verify(token);
      if (!userEmail.equals(expectedUserEmail)) {
        throw new ForbiddenException();
      }
    } catch (FeignException e) {
      if (e.status() == 401) {
        throw new UnauthorizedException();
      } else {
        throw e;
      }
    }
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

  public void updateTarget(int targetId, Target target) throws BadRequestException, NotFoundException {
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
      if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }
  }

  public Iterable<Target> readColocated(){
    return targetsProxy.readAll();
  }
}

