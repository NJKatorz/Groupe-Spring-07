package be.vinci.ipl.projet2024.group07.targets.repositories;

import be.vinci.ipl.projet2024.group07.targets.models.Server;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Proxy pour les serveurs.
 * Fournit des méthodes pour interagir avec le service des serveurs.
 */
@Repository
@FeignClient(name = "servers")
public interface ServersProxy {

  /**
   * Récupère les serveurs par l'identifiant de la cible.
   * @param targetId l'identifiant de la cible.
   * @return une liste de serveurs.
   */
  @GetMapping("/servers/target/{targetId}")
  Iterable<Server> readByTarget(@PathVariable int targetId);

  /**
   * Supprime les serveurs par l'identifiant de la cible.
   * @param targetId l'identifiant de la cible dont les serveurs doivent être supprimés.
   * @return une réponse vide.
   */
  @DeleteMapping("/servers/target/{targetId}")
  public ResponseEntity<Void> deleteByTarget(@PathVariable int targetId);

}
