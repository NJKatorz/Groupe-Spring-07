package be.vinci.ipl.projet2024.group07.mockauthentications;

import jdk.jfr.Enabled;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class MockAuthenticationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockAuthenticationsApplication.class, args);
	}

}
