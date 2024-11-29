package be.vinci.ipl.projet2024.group07.authentications.repositories;

import be.vinci.ipl.projet2024.group07.authentications.models.SafeCredentials;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends CrudRepository<SafeCredentials, String> {
}