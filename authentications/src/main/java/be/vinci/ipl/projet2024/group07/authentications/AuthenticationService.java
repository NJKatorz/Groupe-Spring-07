package be.vinci.ipl.projet2024.group07.authentications;

import be.vinci.ipl.projet2024.group07.authentications.models.SafeCredentials;
import be.vinci.ipl.projet2024.group07.authentications.models.UnsafeCredentials;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

  private final AuthenticationRepository repository;
  private final Algorithm jwtAlgorithm;
  private final JWTVerifier jwtVerifier;

  public AuthenticationService(AuthenticationRepository repository, AuthenticationProperties properties) {
    this.repository = repository;
    this.jwtAlgorithm = Algorithm.HMAC512(properties.getSecret());
    this.jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0").build();
  }


  /**
   * Connects user with credentials
   * @param unsafeCredentials The credentials with insecure password
   * @return The JWT token, or null if the user couldn't be connected
   */
  public String connect(UnsafeCredentials unsafeCredentials) {
    SafeCredentials safeCredentials = repository.findById(unsafeCredentials.getEmail()).orElse(null);
    if (safeCredentials == null) return null;
    if (!BCrypt.checkpw(unsafeCredentials.getPassword(), safeCredentials.getHashedPassword())) return null;
    return JWT.create().withIssuer("auth0").withClaim("pseudo", safeCredentials.getEmail()).sign(jwtAlgorithm);
  }


  /**
   * Verifies JWT token
   * @param token The JWT token
   * @return The email of the user, or null if the token couldn't be verified
   */
  public String verify(String token) {
    try {
      String email = jwtVerifier.verify(token).getClaim("email").asString();
      if (!repository.existsById(email)) return null;
      return email;
    } catch (JWTVerificationException e) {
      return null;
    }
  }


  /**
   * Creates credentials in repository
   * @param unsafeCredentials The credentials with insecure password
   * @return True if the credentials were created, or false if they already exist
   */
  public boolean createOne(UnsafeCredentials unsafeCredentials) {
    if (repository.existsById(unsafeCredentials.getEmail())) return false;
    String hashedPassword = BCrypt.hashpw(unsafeCredentials.getPassword(), BCrypt.gensalt());
    repository.save(unsafeCredentials.makeSafe(hashedPassword));
    return true;
  }

  /**
   * Updates credentials in repository
   * @param unsafeCredentials The credentials with insecure password
   * @return True if the credentials were updated, or false if they couldn't be found
   */
  public boolean updateOne(UnsafeCredentials unsafeCredentials) {
    if (!repository.existsById(unsafeCredentials.getEmail())) return false;
    String hashedPassword = BCrypt.hashpw(unsafeCredentials.getPassword(), BCrypt.gensalt());
    repository.save(unsafeCredentials.makeSafe(hashedPassword));
    return true;
  }

  /**
   * Deletes credentials in repository
   * @param email The email of the user
   * @return True if the credentials were deleted, or false if they couldn't be found
   */
  public boolean deleteOne(String email) {
    if (!repository.existsById(email)) return false;
    repository.deleteById(email);
    return true;
  }
  /**
   * Changes password of user
   * @param request The credentials with insecure password
   * @param newPassword The new password
   * @return True if the password was changed, or false if the user couldn't be found
   */
  public boolean changePassword(UnsafeCredentials request, String newPassword) {
    if (!repository.existsById(request.getEmail())) return false;
    String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    repository.save(request.makeSafe(hashedPassword));
    return true;
  }
}