package be.vinci.ipl.projet2024.group07.gateway.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {
  public NotFoundException() {
    super(HttpStatus.NOT_FOUND);
  }
}