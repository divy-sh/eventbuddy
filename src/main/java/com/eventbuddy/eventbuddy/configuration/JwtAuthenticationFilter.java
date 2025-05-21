package com.eventbuddy.eventbuddy.configuration;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eventbuddy.eventbuddy.model.User;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtGenerator jwtGenerator;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
      throws ServletException, IOException {

    String header = request.getHeader("Authorization");
    String token = null;
    String email = null;

    if (header != null && header.startsWith("Bearer ")) {
      token = header.substring(7);
      if (jwtGenerator.validateToken(token)) {
        email = jwtGenerator.getEmailFromToken(token);
      }
    }

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        User user = new User();
        if (user != null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user, null, List.of()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    filterChain.doFilter(request, response);
  }
}
