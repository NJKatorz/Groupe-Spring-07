package be.vinci.ipl.projet2024.group07.targets;

import be.vinci.ipl.projet2024.group07.targets.models.Server;
import be.vinci.ipl.projet2024.group07.targets.models.Target;
import be.vinci.ipl.projet2024.group07.targets.repositories.AttacksProxy;
import be.vinci.ipl.projet2024.group07.targets.repositories.ServersProxy;
import be.vinci.ipl.projet2024.group07.targets.repositories.TargetsRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * Service pour les cibles.
 * Fournit des méthodes pour gérer les cibles.
 */
@Service
public class TargetsService {
    private final TargetsRepository targetsRepository;
    private final ServersProxy serversProxy;
    private final AttacksProxy attacksProxy;

    /**
     * Constructeur pour initialiser les dépendances.
     * @param targetsRepository le répertoire des cibles.
     * @param serversProxy le proxy pour les serveurs.
     * @param attacksProxy le proxy pour les attaques.
     */
    public TargetsService(TargetsRepository targetsRepository, ServersProxy serversProxy, AttacksProxy attacksProxy) {
        this.targetsRepository = targetsRepository;
        this.serversProxy = serversProxy;
        this.attacksProxy = attacksProxy;
    }

    /**
     * Crée une nouvelle cible.
     * @param target la cible à créer.
     * @return la cible créée, ou null si les données de la cible sont incorrectes.
     */
    public Target createTarget(Target target) {
        if (target.getRevenue() <= 0) return null;
        if (target.getEmployees() <= 0) return null;
        target.setServers(0);
        return targetsRepository.save(target);
    }

    /**
     * Récupère une cible par son identifiant.
     * @param id l'identifiant de la cible.
     * @return la cible correspondant à l'identifiant, ou null si elle n'existe pas.
     */
    public Target getTargetById(int id) {
        return targetsRepository.findById(id).orElse(null);
    }

    /**
     * Récupère toutes les cibles ou celles avec un nombre de serveurs spécifique.
     * @param minServers le nombre minimum de serveurs (optionnel).
     * @param minRevenue le chiffre d'affaire minimum (optionnel).
     * @return une liste de cibles.
     */
    public Iterable<Target> getAllTargets(Integer minServers, Integer minRevenue) {
        int minS = 0;
        int minR = 0;
        if (minServers != null)
            minS = minServers;
        if (minRevenue != null)
            minR = minRevenue;

        return targetsRepository.findAllByServersAndRevenue(minS, minR);
    }

    /**
     * Supprime une cible par son identifiant.
     * @param id l'identifiant de la cible.
     * @return true si la cible a été supprimée, false sinon.
     */
    public boolean deleteTarget(int id) {
        if (!targetsRepository.existsById(id)) return false;
        attacksProxy.deleteTargets(id);
        serversProxy.deleteByTarget(id);
        targetsRepository.deleteById(id);
        return true;
    }

    /**
     * Met à jour une cible.
     * @param target la cible à mettre à jour.
     * @return true si la cible a été mise à jour, false si elle n'existe pas.
     */
    public boolean updateOne(Target target){
        if (!targetsRepository.existsById(target.getId())) return false;
        Target old = targetsRepository.findById(target.getId()).get();
        target.setServers(old.getServers());
        targetsRepository.save(target);
        return true;
    }

    /**
     * Recherche les cibles qui sont hébergées sur des serveurs mutualisés.
     * @return une liste de dictionnaires contenant les adresses IP des serveurs mutualisés et les cibles qui y sont hébergées.
     */
    public Iterable<Map<String, List<Target>>> getIpColocated() {
        Map<String, Set<Target>> dicoIp = new HashMap<>();
        for (Target target : targetsRepository.findAll()) {
            Iterable<Server> servers = serversProxy.readByTarget(target.getId());
            System.out.println(servers);
            for (Server server : servers) {
                Set<Target> targetSet = dicoIp.computeIfAbsent(server.getIpAddress(), k -> new HashSet<>());
                targetSet.add(target);
            }
        }

        System.out.printf("dicoIp: %s\n", dicoIp);

        ArrayList<Map<String, List<Target>>> allIp = new ArrayList<>();
        for (String ip : dicoIp.keySet()) {
            if (dicoIp.get(ip).size() > 1) {
                Map<String, List<Target>> ipMap = new HashMap<>();
                ipMap.put(ip, new ArrayList<>(dicoIp.get(ip)));
                allIp.add(ipMap);
            }
        }
        return allIp;
    }
}
