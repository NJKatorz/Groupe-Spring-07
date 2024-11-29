package be.vinci.ipl.projet2024.group07.targets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Classe principale de l'application Targets.
 * Configure et lance l'application Spring Boot.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class TargetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TargetsApplication.class, args);
	}

}
