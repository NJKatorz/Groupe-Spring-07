package be.vinci.ipl.projet2024.group07.attack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class AttackApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttackApplication.class, args);
	}
}
