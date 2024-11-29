package be.vinci.ipl.projet2024.group07.servers;

import be.vinci.ipl.projet2024.group07.servers.models.Server;
import be.vinci.ipl.projet2024.group07.servers.models.Target;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ServerController {

  private final ServerService serverService;

  public ServerController(ServerService serverService) {
    this.serverService = serverService;
  }


  /**
   * Récupère tous les serveurs.
   * @return une liste de serveurs.
   */
  @GetMapping("/servers/{serverId}")
  public ResponseEntity<Server> readOne(@PathVariable int serverId) {
    Server server = serverService.getOne(serverId);
    if (server == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(server, HttpStatus.OK);
  }

  /**
   * Crée un nouveau serveur.
   *
   * @param newServer le nouveau serveur à créer.
   * @return une réponse HTTP avec le serveur créé
   * et le statut HTTP CREATED si la création est réussie,
   * ou une réponse HTTP avec le statut BAD_REQUEST si les données du
   * serveur sont invalides ou si la création échoue.
   */
  @PostMapping("/servers")
  public ResponseEntity<Server> createOne(@RequestBody Server newServer) {
    if (newServer.getIpAddress()==null || newServer.getServerType()==null || newServer.getTechnology()==null
        || newServer.getTargetId() == 0) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    newServer.setValidated(false);
    Server server = serverService.createOne(newServer);
    if (server == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(server, HttpStatus.CREATED);
  }


  /**
   * Met à jour un serveur existant.
   *
   * @param serverId l'ID du serveur à mettre à jour.
   * @param updatedServer le serveur mis à jour.
   * @return une réponse HTTP avec le statut NO_CONTENT si la mise à jour est réussie,
   * ou une réponse HTTP avec le statut BAD_REQUEST si les données du serveur sont invalides,
   * ou une réponse HTTP avec le statut NOT_FOUND si le serveur n'existe pas.
   */
  @PutMapping("/servers/{serverId}")
  public ResponseEntity<Void> updateOne(@PathVariable int serverId, @RequestBody Server updatedServer) {
    if (updatedServer.getIpAddress()==null || updatedServer.getServerType()==null || updatedServer.getTechnology()==null
       || serverId != updatedServer.getId() ) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    Server server = serverService.getOne(serverId);
    if (server == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    if (server.getTargetId() !=  updatedServer.getTargetId() || server.isValidated() != updatedServer.isValidated()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    serverService.updateOne(updatedServer);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Supprime un serveur existant.
   *
   * @param serverId l'ID du serveur à supprimer.
   * @return une réponse HTTP avec le statut NO_CONTENT si la suppression est réussie,
   * ou une réponse HTTP avec le statut NOT_FOUND si le serveur n'existe pas.
   */
  @DeleteMapping("/servers/{serverId}")
  public ResponseEntity<Void> deleteOne(@PathVariable int serverId) {
    Server server =serverService.deleteOne(serverId);
    if (server == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }


  /**
   * Valide un serveur existant.
   *
   * @param serverId l'ID du serveur à valider.
   * @return une réponse HTTP avec le statut NO_CONTENT si la validation est réussie,
   * ou une réponse HTTP avec le statut NOT_FOUND si le serveur n'existe pas,
   * ou une réponse HTTP avec le statut BAD_REQUEST si le serveur est déjà validé.
   */
  @PatchMapping("/servers/{serverId}/validate")
  public ResponseEntity<Void> validateServer(@PathVariable int serverId) {
    Server server = serverService.getOne(serverId);
    if (server == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    if (server.isValidated()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    serverService.validateServer(server);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }


  /**
   * Récupère tous les serveurs associés à une cible.
   *
   * @param targetId l'ID de la cible.
   * @return une réponse HTTP avec la liste des serveurs et le statut HTTP OK.
   */
  @GetMapping("/servers/target/{targetId}")
  public ResponseEntity<Iterable<Server>> readByTarget(@PathVariable int targetId) {
    Iterable<Server> servers = serverService.getByTarget(targetId);
    return new ResponseEntity<>(servers, HttpStatus.OK);
  }


  /**
   * Supprime tous les serveurs associés à une cible.
   *
   * @param targetId l'ID de la cible.
   * @return une réponse HTTP avec le statut NO_CONTENT si la suppression est réussie.
   */
  @DeleteMapping("/servers/target/{targetId}")
  public ResponseEntity<Void> deleteByTarget(@PathVariable int targetId) {
    serverService.deleteByTarget(targetId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
