package be.vinci.ipl.projet2024.group07.attack.repositories;

import be.vinci.ipl.projet2024.group07.attack.models.Server;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servers")
public interface ServerProxy {

  /**
   * Récupère un serveur en fonction de son ID.
   * @param serverId l'ID du serveur à récupérer.
   * @return le serveur correspondant.
   */
  @GetMapping("/servers/{serverId}")
  Server readOne(@PathVariable int serverId);


}
