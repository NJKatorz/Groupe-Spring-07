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


  /**
   * Récupère un serveur en fonction de son ID.
   * @param serverId l'ID du serveur à récupérer.
   * @return le serveur correspondant.
   */
  public Server getOne(int serverId) {return repository.findById(serverId).orElse(null);}


  /**
   * Crée un nouveau serveur.
   *
   * @param newServer le nouveau serveur à créer.
   * @return le serveur créé, ou null si la cible associée n'existe pas.
   */
  public Server createOne(Server newServer) {
    if (targetProxy.readOne(newServer.getTargetId()) == null) {
      return null;
    }
    targetProxy.increaseServers(newServer.getTargetId());
    return repository.save(newServer);
  }

  /**
   * Met à jour un serveur existant.
   *
   * @param updatedServer le serveur mis à jour.
   */
  public void updateOne(Server updatedServer) {
    repository.save(updatedServer);
  }

  /**
   * Supprime un serveur en fonction de son ID.
   * @param serverId l'ID du serveur à supprimer.
   * @return le serveur supprimé, ou null si aucun serveur n'est trouvé pour cet ID.
   */
  public Server deleteOne(int serverId) {
    Server server = repository.findById(serverId).orElse(null);
    if (server == null) {
      return null;
    }
    targetProxy.decreaseServers(server.getTargetId());
    repository.deleteById(serverId);
    return server;
  }


  /**
   * Valide un serveur existant.
   *
   * @param server le serveur à valider.
   */
  public void validateServer(Server server) {
    server.setValidated(true);
    repository.save(server);

  }

  /**
   * Récupère tous les serveurs associés à une cible.
   *
   * @param targetId l'ID de la cible.
   * @return les serveurs associés à la cible.
   */
  public Iterable<Server> getByTarget(int targetId) {
    return repository.findByTargetId(targetId);
  }


  /**
   * Supprime tous les serveurs associés à une cible.
   *
   * @param targetId l'ID de la cible.
   */
  public void deleteByTarget(int targetId) {
    repository.findByTargetId(targetId).forEach(server -> attackProxy.deleteByServer(server.getId()));
    repository.deleteByTargetId(targetId);

  }
}
