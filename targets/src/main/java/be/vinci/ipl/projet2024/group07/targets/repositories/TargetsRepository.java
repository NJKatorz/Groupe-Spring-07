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
   * Trouve les cibles ayant un nombre de serveurs et un chiffre d'affaires supérieur ou égal aux valeurs données.
   * @param minS le nombre minimum de serveurs.
   * @param minR le nombre minimum de chiffre d'affaires.
   * @return une liste de cibles correspondant aux contraintes données.
   */

  @Query("SELECT t FROM targets t WHERE t.servers >= ?1 AND t.revenue >= ?2")
  Iterable<Target> findAllByServersAndRevenue(int minS, int minR);
}
