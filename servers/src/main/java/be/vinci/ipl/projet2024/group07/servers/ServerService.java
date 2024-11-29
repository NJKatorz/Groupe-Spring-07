package be.vinci.ipl.projet2024.group07.servers;

import be.vinci.ipl.projet2024.group07.servers.models.Server;
import be.vinci.ipl.projet2024.group07.servers.models.Target;
import be.vinci.ipl.projet2024.group07.servers.repositories.AttackProxy;
import be.vinci.ipl.projet2024.group07.servers.repositories.ServerRepository;
import be.vinci.ipl.projet2024.group07.servers.repositories.TargetProxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServerService {
  private final ServerRepository repository;
  private final TargetProxy targetProxy;
  private final AttackProxy attackProxy;


  public ServerService(ServerRepository repository, TargetProxy targetProxy,
      AttackProxy attackProxy) {
    this.repository = repository;
    this.targetProxy = targetProxy;
    this.attackProxy = attackProxy;
  }


  public Server getOne(int serverId) {return repository.findById(serverId).orElse(null);}



  public Server createOne(Server newServer) {
    if (targetProxy.readOne(newServer.getTargetId()) == null) {
      return null;
    }
    targetProxy.increaseServers(newServer.getTargetId());
    return repository.save(newServer);
  }

  public void updateOne(Server updatedServer) {
    repository.save(updatedServer);
  }

  public Server deleteOne(int serverId) {
    Server server = repository.findById(serverId).orElse(null);
    if (server == null) {
      return null;
    }
    targetProxy.decreaseServers(server.getTargetId());
    repository.deleteById(serverId);
    return server;
  }

  public void validateServer(Server server) {
    server.setValidated(true);
    repository.save(server);

  }

  public Iterable<Server> getByTarget(int targetId) {
    return repository.findByTargetId(targetId);
  }

  public void deleteByTarget(int targetId) {
    repository.findByTargetId(targetId).forEach(server -> attackProxy.deleteByServer(server.getId()));
    repository.deleteByTargetId(targetId);

  }
}
