package be.vinci.ipl.projet2024.group07.targets;

import be.vinci.ipl.projet2024.group07.targets.models.Server;
import be.vinci.ipl.projet2024.group07.targets.models.Target;
import be.vinci.ipl.projet2024.group07.targets.repositories.ServersProxy;
import be.vinci.ipl.projet2024.group07.targets.repositories.TargetsRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TargetsService {
    private TargetsRepository targetsRepository;
    private final ServersProxy serversProxy;

    public TargetsService(TargetsRepository targetsRepository, ServersProxy serversProxy) {
        this.targetsRepository = targetsRepository;
        this.serversProxy = serversProxy;
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
        return targetsRepository.findAll();
    }

    public void deleteTarget(int id) {
        targetsRepository.deleteById(id);
    }

    public boolean updateOne(Target target){
        if (!targetsRepository.existsById(target.getId())) return false;
        targetsRepository.save(target);
        return true;
    }

    public Iterable<String> getIpColocated() {
        Map<String, Set<Target>> dicoIp = new HashMap();
        for (Target target : targetsRepository.findAll()) {
            Iterable<Server> servers = serversProxy.readByTarget(target.getId());
            for (Server server : servers) {
                if (dicoIp.containsKey(server.getIpAddress())) {
                    dicoIp.get(server.getIpAddress()).add(target);
                } else {
                    Set<Target> targets = Set.of(target);
                    dicoIp.put(server.getIpAddress(), targets);
                }
            }
        }

        ArrayList<String> allIp = new ArrayList<>();
        for (String ip : dicoIp.keySet()) {
            if (dicoIp.get(ip).size() > 1) {
                allIp.add(ip);
            }
        }
        return allIp;
    }
}
