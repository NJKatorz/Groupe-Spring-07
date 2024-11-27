package be.vinci.ipl.projet2024.group07.targets;

import be.vinci.ipl.projet2024.group07.targets.models.Target;
import be.vinci.ipl.projet2024.group07.targets.repositories.TargetsRepository;
import java.util.ArrayList;

public class TargetsService {
    private TargetsRepository targetsRepository;

    public TargetsService(TargetsRepository targetsRepository) {
        this.targetsRepository = targetsRepository;
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

    public Iterable<Target> getAllTargets() {
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

    public Iterable<Target> getColocatedTargets() {
        return new ArrayList<>();
    }
}
