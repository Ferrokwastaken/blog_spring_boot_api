package blog_api.test.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

// Spring component, so it can be injected
@Component
public class JwtUtil {
  // Secret key and expiration time from application.properties
  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration; // Milliseconds

  // Generate a token for a given username
  public String generateToken(String username) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, username);
  }

  // Building JWTs
  private String createToken(Map<String, Object> extraClaims, String subject) {
    return Jwts.builder()
        .claims(extraClaims) // Sets the custom claims map
        .subject(subject) // Sets the 'sub' (subject) claim
        .issuedAt(new Date(System.currentTimeMillis())) // Sets the 'iat' (issued at) claim
        .expiration(new Date(System.currentTimeMillis() + expiration)) // Sets the 'exp' (expiration) claim
        .signWith(getSigningKey()) // Use the simplified signWith(Key)
        .compact();
  }

  // Get the signing key from the secret string
  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // --- Token Validation and Information Extraction ---

  // Validate the token
  public Boolean validateToken(String token, String username) {
    try {
      final String extractedUsername = extractUsername(token);
      return (extractedUsername.equals(username) && !isTokenExpired(token));
    } catch (Exception e) {
      System.err.println("JWT Validation Error: " + e.getMessage());
      return false;
    }
  }

  // Extract username from token
  public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
  }

  // Extract expiration date from token
  public Date extractExpiration(String token) {
    return extractAllClaims(token).getExpiration();
  }

  // Check if the token is expired
  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  // Parsing tokens and getting the payload
  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
