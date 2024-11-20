package be.vinci.ipl.projet2024.group07.gateway.data;

import be.vinci.ipl.projet2024.group07.gateway.models.Target;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "targets")
public interface TargetsProxy {

  @GetMapping("/targets")
  Iterable<Target> readAll();

  @PostMapping("/targets")
  void createOne(@RequestBody Target target);

  @GetMapping("/targets/{targetId}")
  Target readOne(@PathVariable int targetId);

  @PutMapping("/targets/{targetId}")
  void updateOne(@PathVariable int targetId, @RequestBody Target target);

  @DeleteMapping("/targets/{targetId}")
  void deleteOne(@PathVariable int targetId);

  @GetMapping("/targets/colocated")
  Iterable<Target> readColocated();
}
