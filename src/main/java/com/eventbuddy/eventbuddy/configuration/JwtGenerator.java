package com.eventbuddy.eventbuddy.configuration;

import com.eventbuddy.eventbuddy.model.User;
import com.eventbuddy.eventbuddy.model.UserToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  public UserToken generateToken(User user) {
    String token = Jwts.builder()
        .setSubject(user.getEmail())
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
    return new UserToken(user, token);
  }

  // Other JWT-related methods (e.g., validation, parsing, etc.) can be added here
}