package be.vinci.ipl.projet2024.group07.servers.repositories;

import be.vinci.ipl.projet2024.group07.servers.models.Target;
import jakarta.transaction.Transactional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "targets")
public interface TargetProxy {

  @GetMapping("/targets/{targetId}")
  Target readOne(@PathVariable int targetId);


  @PatchMapping("/targets/{targetId}/increase-servers")
  @Transactional
  void increaseServers(@PathVariable int targetId);//erreur avec Patch mais pas Put


  @PatchMapping("/targets/{targetId}/decrease-servers")
  @Transactional
  void decreaseServers(@PathVariable int targetId);//erreur avec Patch mais pas Put
}