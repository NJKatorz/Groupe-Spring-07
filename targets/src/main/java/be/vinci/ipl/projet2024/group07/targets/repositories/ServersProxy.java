package be.vinci.ipl.projet2024.group07.targets.repositories;

import be.vinci.ipl.projet2024.group07.targets.models.Server;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "servers")
public interface ServersProxy {

  @GetMapping("/servers/target/{targetId}")
  Iterable<Server> readByTarget(@PathVariable int targetId);

  @DeleteMapping("/servers/target/{targetId}")
  public ResponseEntity<Void> deleteByTarget(@PathVariable int targetId);

}
