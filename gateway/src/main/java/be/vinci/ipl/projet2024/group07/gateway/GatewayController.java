package be.vinci.ipl.projet2024.group07.gateway;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {
  private final GatewayService service;

  public GatewayController(GatewayService service) {
    this.service = service;
  }

}
