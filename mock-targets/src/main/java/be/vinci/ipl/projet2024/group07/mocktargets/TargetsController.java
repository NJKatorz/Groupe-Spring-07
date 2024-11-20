package be.vinci.ipl.projet2024.group07.mocktargets;

import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TargetsController {

  @GetMapping("/targets")
  public Iterable<Target> readAll() {
    return new ArrayList<>();
  }

  @PostMapping("/targets")
  public ResponseEntity<Void> createOne(@RequestBody Target target) {
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/targets/{targetId}")
  public Target readOne(@PathVariable int targetId) {
    Target target = new Target();
    target.setId(targetId);
    target.setCompanyName("CyberCorp");
    target.setDomain("cybercorp.com");
    target.setLocation("Paris, France");
    target.setEmployees(100);
    target.setServers(5);
    target.setRevenue(1000000.0);
    return target;
  }

  @PutMapping("/targets/{targetId}")
  public void updateOne(@PathVariable int targetId, @RequestBody Target target) {
  }

  @DeleteMapping("/targets/{targetId}")
  public void deleteOne(@PathVariable int targetId) {
  }

  @PatchMapping("/targets/{targetId}/increase-servers")
  public void increaseServers(@PathVariable int targetId) {
  }

  @PatchMapping("/targets/{targetId}/decrease-servers")
  public void decreaseServers(@PathVariable int targetId) {
  }

  @GetMapping("/targets/colocated")
  public Iterable<Target> readColocated() {
    return new ArrayList<>();
  }
}
