package be.vinci.ipl.projet2024.group07.authentications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AuthenticationsApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthenticationsApplication.class, args);
  }

}
