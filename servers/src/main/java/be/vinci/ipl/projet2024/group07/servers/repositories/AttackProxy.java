package be.vinci.ipl.projet2024.group07.servers.repositories;

import jakarta.transaction.Transactional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "attacks")
public interface AttackProxy {


  /**
   * Supprime les attaques liées à un serveur.
   * @param serverId l'ID du serveur.
   */
  @DeleteMapping("/attacks/servers/{serverId}")
  @Transactional
  void deleteByServer(@PathVariable int serverId);

}
