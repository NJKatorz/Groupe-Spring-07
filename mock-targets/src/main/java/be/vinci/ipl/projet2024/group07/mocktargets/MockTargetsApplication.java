package be.vinci.ipl.projet2024.group07.mocktargets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MockTargetsApplication {

  public static void main(String[] args) {
    SpringApplication.run(MockTargetsApplication.class, args);
  }

}
