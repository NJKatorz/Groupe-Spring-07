package be.vinci.ipl.projet2024.group07.mockservers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MockServersApplication {
  public static void main(String[] args) {
    SpringApplication.run(MockServersApplication.class, args);
  }

}
