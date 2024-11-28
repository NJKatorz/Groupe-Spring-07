package be.vinci.ipl.projet2024.group07.servers.repositories;

import be.vinci.ipl.projet2024.group07.servers.models.Target;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "targets")
public interface TargetProxy {
  @GetMapping("/targets/{targetId}")
  Target readOne(@PathVariable int targetId);
}
