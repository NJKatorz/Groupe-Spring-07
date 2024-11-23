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

  @PostMapping("/servers")
  void createOne(@RequestBody Server server);

  @GetMapping("servers/{serverId}")
  Server readOne(@PathVariable int serverId);

  @PutMapping("servers/{serverId}")
  void updateOne(@PathVariable int serverId, @RequestBody Server server);

  @DeleteMapping("servers/{serverId}")
  void deleteOne(@PathVariable int serverId);

  @PatchMapping("/servers/{serverId}/validate")
  void validateServer(@PathVariable int serverId);

  @GetMapping("/servers/target/{targetId}")
  Iterable<Server> readAllServersByTargetId(@PathVariable int targetId);
}
