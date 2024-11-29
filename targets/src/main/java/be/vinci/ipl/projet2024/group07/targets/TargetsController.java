package be.vinci.ipl.projet2024.group07.targets;

import be.vinci.ipl.projet2024.group07.targets.models.Target;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Contrôleur pour les cibles.
 * Fournit des points de terminaison pour gérer les cibles.
 */
@RestController
@RequestMapping("/targets")
public class TargetsController {
  private final TargetsService service;

  /**
   * Constructeur pour initialiser le service des cibles.
   * @param service le service des cibles.
   */
  public TargetsController(TargetsService service) {
    this.service = service;
  }

  /**
   * Récupère toutes les cibles ou celles correspondant au filtre donné.
   * @param minServers le nombre minimum de serveurs (optionnel).
   * @param maxServers le nombre maximum de serveurs (optionnel).
   * @return une liste de cibles.
   */
  @GetMapping
  public Iterable<Target> getAllTargets(
      @RequestParam(required = false) Integer minServers,
      @RequestParam(required = false) Integer maxServers) {
    return service.getAllTargets(minServers, maxServers);
  }

  /**
   * Crée une nouvelle cible.
   * @param target la cible à créer.
   * @return une réponse contenant la cible créée.
   */
  @PostMapping
  public ResponseEntity<Target> createOne(@RequestBody Target target) {
    if (target.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les données de la cible sont manquantes ou incorrectes");

    Target newTarget = service.createTarget(target);
    if (newTarget == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    return new ResponseEntity<>(newTarget, HttpStatus.CREATED);
  }

  /**
   * Récupère une cible par son identifiant.
   * @param targetId l'identifiant de la cible.
   * @return une réponse contenant la cible.
   */
  @GetMapping("/{targetId}")
  public Target readOne(@PathVariable int targetId) {
    Target target = service.getTargetById(targetId);
    if (target == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune cible trouvée pour cet ID");
    return target;
  }

  /**
   * Met à jour une cible.
   * @param targetId l'identifiant de la cible.
   * @param target la cible à mettre à jour.
   * @return une réponse de la requête.
   */
  @PutMapping("/{targetId}")
  public ResponseEntity<Void> updateOne(@PathVariable int targetId, @RequestBody Target target) {
    if (!Objects.equals(target.getId(), targetId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les données de la cible sont incorrectes");
    if (target.invalid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les données de la cible sont manquantes ou incorrectes");

    boolean found = service.updateOne(target);
    if (!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune cible trouvée pour cet ID");
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Supprime une cible par son identifiant.
   * @param targetId l'identifiant de la cible.
   * @return une réponse de la requête.
   */
  @DeleteMapping("/{targetId}") // Problème avec le proxy server
  public ResponseEntity<Void> deleteOne(@PathVariable int targetId) {
    if (service.deleteTarget(targetId))
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune cible trouvée pour cet ID");
  }

  /**
   * Augmente le nombre de serveurs d'une cible.
   * @param targetId l'identifiant de la cible.
   * @return une réponse de la requête.
   */
  @PatchMapping("/{targetId}/increase-servers")
  public ResponseEntity<Void> increaseServers(@PathVariable int targetId) {
    Target target = service.getTargetById(targetId);
    if (target == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune cible trouvée pour cet ID");
    target.setServers(target.getServers() + 1);
    service.updateOne(target);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Diminue le nombre de serveurs d'une cible.
   * @param targetId l'identifiant de la cible.
   * @return une réponse de la requête.
   */
  @PatchMapping("/{targetId}/decrease-servers")
  public ResponseEntity<Void> decreaseServers(@PathVariable int targetId) {
    Target target = service.getTargetById(targetId);
    if (target == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune cible trouvée pour cet ID");
    target.setServers(target.getServers() - 1);
    service.updateOne(target);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Récupère les cibles qui sont hébergées sur des serveurs mutualisés.
   * @return une liste des adresses IP des serveurs ainsi que les cibles qui sont hébergées dessus.
   */
  @GetMapping("/colocated")
  public Iterable<Map<String, List<Target>>> readColocated() {
    return service.getIpColocated();
  }
}
