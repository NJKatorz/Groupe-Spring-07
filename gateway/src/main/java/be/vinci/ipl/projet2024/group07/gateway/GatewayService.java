package be.vinci.ipl.projet2024.group07.gateway;

import be.vinci.ipl.projet2024.group07.gateway.data.AttacksProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.AuthenticationProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.ExploitsProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.ServersProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.TargetsProxy;
import be.vinci.ipl.projet2024.group07.gateway.data.UsersProxy;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.BadRequestException;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.ForbiddenException;
import be.vinci.ipl.projet2024.group07.gateway.exceptions.UnauthorizedException;
import be.vinci.ipl.projet2024.group07.gateway.models.Credentials;
import feign.FeignException;
import org.springframework.stereotype.Service;

@Service
public class GatewayService {

  private final AttacksProxy attacksProxy;
  private final AuthenticationProxy authenticationProxy;
  private final ExploitsProxy exploitsProxy;
  private final ServersProxy serversProxy;
  private final TargetsProxy targetsProxy;
  private final UsersProxy usersProxy;

  public GatewayService(AttacksProxy attacksProxy, AuthenticationProxy authenticationProxy, ExploitsProxy exploitsProxy, ServersProxy serversProxy, TargetsProxy targetsProxy, UsersProxy usersProxy) {
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

  public void verify(String token, String expectedUserEmail) throws ForbiddenException, UnauthorizedException {
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

}