package be.vinci.ipl.projet2024.group07.targets;

import be.vinci.ipl.projet2024.group07.targets.models.Target;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TargetsController {
  private final TargetsService service;

  public TargetsController(TargetsService service) {
    this.service = service;
  }

  @GetMapping("/targets") // vérifier que le filtre fonctionne
  public Iterable<Target> getAllTargets(
      @RequestParam(required = false) Integer minServers,
      @RequestParam(required = false) Integer maxServers) {
    return service.getAllTargets(minServers, maxServers);
  }

  @PostMapping("/targets")
  public ResponseEntity<Target> createOne(@RequestBody Target target) {
    if (target.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les données de la cible sont manquantes ou incorrectes");

    Target newTarget = service.createTarget(target);
    if (newTarget == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    return new ResponseEntity<>(newTarget, HttpStatus.CREATED);
  }

  @GetMapping("/targets/{targetId}")
  public Target readOne(@PathVariable int targetId) {
    Target target = service.getTargetById(targetId);
    if (target == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune cible trouvée pour cet ID");
    return target;
  }

  @PutMapping("/targets/{targetId}")
  public ResponseEntity<Void> updateOne(@PathVariable int targetId, @RequestBody Target target) {
    if (!Objects.equals(target.getId(), targetId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les données de la cible sont incorrectes");
    if (target.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les données de la cible sont manquantes ou incorrectes");

    boolean found = service.updateOne(target);
    if (!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune cible trouvée pour cet ID");
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/targets/{targetId}") // tester avec le proxy
  public ResponseEntity<Void> deleteOne(@PathVariable int targetId) {
    if (service.deleteTarget(targetId))
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune cible trouvée pour cet ID");
  }

  @PatchMapping("/targets/{targetId}/increase-servers")
  public ResponseEntity<Void> increaseServers(@PathVariable int targetId) {
    Target target = service.getTargetById(targetId);
    if (target == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune cible trouvée pour cet ID");
    target.setServers(target.getServers() + 1);
    service.updateOne(target);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PatchMapping("/targets/{targetId}/decrease-servers")
  public ResponseEntity<Void> decreaseServers(@PathVariable int targetId) {
    Target target = service.getTargetById(targetId);
    if (target == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune cible trouvée pour cet ID");
    target.setServers(target.getServers() - 1);
    service.updateOne(target);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/targets/colocated") // vérifier avec le proxy
  public Iterable<String> readColocated() {
    return service.getIpColocated();
  }
}
