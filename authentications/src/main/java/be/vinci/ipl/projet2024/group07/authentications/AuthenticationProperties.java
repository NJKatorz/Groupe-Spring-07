package be.vinci.ipl.projet2024.group07.authentications;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "be.vinci.ipl.projet2024.group07.authentications")
public class AuthenticationProperties {
  private String secret;
}