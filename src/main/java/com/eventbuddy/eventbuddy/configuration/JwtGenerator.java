package com.eventbuddy.eventbuddy.configuration;

import com.eventbuddy.eventbuddy.model.User;
import com.eventbuddy.eventbuddy.model.UserToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;

@Component
public class JwtGenerator {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  private Key getSigningKey() {
    byte[] keyBytes = Base64.getUrlDecoder().decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public UserToken generateToken(User user) {
    String token = Jwts.builder()
        .setSubject(user.getEmail())
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
    return new UserToken(user, token);
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public String getEmailFromToken(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claims.getSubject();
  }
}