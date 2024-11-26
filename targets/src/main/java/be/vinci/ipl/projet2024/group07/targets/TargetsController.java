package be.vinci.ipl.projet2024.group07.targets;

import be.vinci.ipl.projet2024.group07.targets.models.Target;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

public class TargetsController {
  private final TargetsService service;

  public TargetsController(TargetsService service) {
    this.service = service;
  }

  @GetMapping("/targets")
  public Iterable<Target> getAllTargets() {
    return service.getAllTargets();
  }

  @PostMapping("/targets")
  public ResponseEntity<Void> createOne(@RequestBody Target target) {
    if (target.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les données de la cible sont manquantes ou incorrectes");

    if (service.createTarget(target) == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/targets/{targetId}")
  public Target readOne(@PathVariable int targetId) {
    Target target = service.getTargetById(targetId);
    if (target == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune cible trouvée pour cet ID");
    return target;
  }

  @PutMapping("/targets/{targetId}")
  public void updateOne(@PathVariable int targetId, @RequestBody Target target) {
    if (!Objects.equals(target.getId(), targetId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    if (target.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les données de la cible sont manquantes ou incorrectes");

    boolean found = service.updateOne(target);
    if (!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune cible trouvée pour cet ID");
  }
}