package be.vinci.ipl.projet2024.group07.targets.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Proxy pour les attaques.
 * Fournit des méthodes pour interagir avec le service des attaques.
 */
@Repository
@FeignClient(name = "attacks")
public interface AttacksProxy {

  /**
   * Supprime les attaques par l'identifiant de la cible.
   * @param targetId l'identifiant de la cible dont les attaques doivent être supprimées.
   */
  @DeleteMapping("/attack/targets/{targetId}")
  public void deleteTargets(@PathVariable int targetId);

}
