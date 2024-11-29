package be.vinci.ipl.projet2024.group07.gateway.data;

import be.vinci.ipl.projet2024.group07.gateway.models.Server;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "servers")
public interface ServersProxy {

  /**
   * Create a server
   * @param server the server to create
   */
  @PostMapping("/servers")
  void createOne(@RequestBody Server server);

  /**
   * Get one server
   * @param serverId the id of the server to get
   */
  @GetMapping("/servers/{serverId}")
  Server readOne(@PathVariable int serverId);

  /**
   * Update a server
   * @param serverId the id of the server to update
   * @param server the new server
   */
  @PutMapping("/servers/{serverId}")
  void updateOne(@PathVariable int serverId, @RequestBody Server server);

  /**
   * Delete one server
   * @param serverId the id of the server to delete
   */
  @DeleteMapping("/servers/{serverId}")
  void deleteOne(@PathVariable int serverId);

  /**
   * Validate a server
   * @param serverId the id of the server to validate
   */
  @PatchMapping("/servers/{serverId}/validate")
  void validateServer(@PathVariable int serverId);

  /**
   * Get all servers by target id
   * @return all servers by target id
   */
  @GetMapping("/servers/target/{targetId}")
  Iterable<Server> readAllServersByTargetId(@PathVariable int targetId);
}
