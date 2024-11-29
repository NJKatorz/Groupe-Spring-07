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

@Service
public class TargetsService {
    private final TargetsRepository targetsRepository;
    private final ServersProxy serversProxy;
    private final AttacksProxy attacksProxy;

    public TargetsService(TargetsRepository targetsRepository, ServersProxy serversProxy, AttacksProxy attacksProxy) {
        this.targetsRepository = targetsRepository;
        this.serversProxy = serversProxy;
        this.attacksProxy = attacksProxy;
    }

    public Target createTarget(Target target) {
        if (target.getRevenue() <= 0) return null;
        if (target.getEmployees() <= 0) return null;
        target.setServers(0);
        return targetsRepository.save(target);
    }

    public Target getTargetById(int id) {
        return targetsRepository.findById(id).orElse(null);
    }

    public Iterable<Target> getAllTargets(Integer minServers, Integer maxServers) {
        int min = 0;
        int max = Integer.MAX_VALUE;
        if (minServers != null)
            min = minServers;
        if (maxServers != null)
            max = maxServers;

        return targetsRepository.findAllByServersBetween(min, max);
    }

    public boolean deleteTarget(int id) {
        if (!targetsRepository.existsById(id)) return false;
        targetsRepository.deleteById(id);
        serversProxy.deleteByTarget(id);
        attacksProxy.deleteTargets(id);
        return true;
    }

    public boolean updateOne(Target target){
        if (!targetsRepository.existsById(target.getId())) return false;
        targetsRepository.save(target);
        return true;
    }

    public Iterable<String> getIpColocated() {
        System.out.println("test");
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

        ArrayList<String> allIp = new ArrayList<>();
        for (String ip : dicoIp.keySet()) {
            if (dicoIp.get(ip).size() > 1) {
                allIp.add(ip);
            }
        }
        return allIp;
    }
}
