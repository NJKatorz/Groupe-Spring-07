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
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(name = "targets")
public interface TargetsProxy {

  /**
   * Get all targets
   * @param minServers the minimum number of servers to filter
   * @param minRevenue the minimum revenue to filter
   * @return all targets
   */
  @GetMapping("/targets")
  Iterable<Target> getAllTargets(@RequestParam(required = false) Integer minServers,
      @RequestParam(required = false) Integer minRevenue);

  /**
   * Create a target
   * @param target
   */
  @PostMapping("/targets")
  void createOne(@RequestBody Target target);

  /**
   * Get one target
   * @param targetId the id of the target to get
   * @return the target
   */
  @GetMapping("/targets/{targetId}")
  Target readOne(@PathVariable int targetId);

  /**
   * Update a target
   * @param targetId the id of the target to update
   * @param target the new target
   */
  @PutMapping("/targets/{targetId}")
  void updateOne(@PathVariable int targetId, @RequestBody Target target);

  /**
   * Delete one target
   * @param targetId the id of the target to delete
   */
  @DeleteMapping("/targets/{targetId}")
  void deleteOne(@PathVariable int targetId);

  /**
   * Get all colocated targets
   * @return all colocated targets
   */
  @GetMapping("/targets/colocated")
  Iterable<String> readColocated();
}
