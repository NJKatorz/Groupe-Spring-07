package be.vinci.ipl.projet2024.group07.targets.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "attacks")
public interface AttacksProxy {

  @DeleteMapping("/attack/targets/{targetId}")
  public void deleteTargets(@PathVariable int targetId);

}
