package be.vinci.ipl.projet2024.group07.targets.repositories;

import be.vinci.ipl.projet2024.group07.targets.models.Server;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "users")
public interface ServersProxy {

  @GetMapping("/servers/target/{targetId}")
  Iterable<Server> readByTarget(@PathVariable int targetId);

}
