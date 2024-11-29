package be.vinci.ipl.projet2024.group07.attack.repositories;

import be.vinci.ipl.projet2024.group07.attack.models.Target;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "targets")
public interface TargetProxy {

  @GetMapping("/targets/{targetId}")
  Target readOne(@PathVariable int targetId);
}