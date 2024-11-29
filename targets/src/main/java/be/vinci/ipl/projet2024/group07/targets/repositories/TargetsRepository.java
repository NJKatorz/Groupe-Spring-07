package be.vinci.ipl.projet2024.group07.targets.repositories;

import be.vinci.ipl.projet2024.group07.targets.models.Target;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Répertoire pour les cibles.
 * Fournit des méthodes pour interagir avec les données des cibles.
 */
public interface TargetsRepository extends CrudRepository<Target, Integer> {

  /**
   * Trouve les cibles ayant un nombre de serveurs compris entre les 2 paramètres.
   * @param min le nombre minimum de serveurs.
   * @param max le nombre maximum de serveurs.
   * @return une liste de cibles correspondant à la contrainte donnée.
   */
  @Query("select t from targets t where t.servers >= ?1 and t.servers <= ?2")
  Iterable<Target> findAllByServersBetween(int min, int max);
}
