package be.vinci.ipl.projet2024.group07.gateway.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;

@Repository
@FeignClient(name = "targets")
public interface TargetsProxy {

}
