package com.eventbuddy.eventbuddy.configuration;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.dao.UserDao;
import com.eventbuddy.eventbuddy.model.User;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtGenerator jwtGenerator;

  @Autowired
  private UserDao userDao;

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
        User user;
        try {
          user = userDao.getUserDetail(email);
        } catch (BuddyError e) {
          user = null;
        }
        if (user != null) {
            List<SimpleGrantedAuthority> authorities = new java.util.ArrayList<>();
            if (user.isAdmin()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            if (user.isOrganizer()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ORGANIZER"));
            }
            
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    filterChain.doFilter(request, response);
  }
}
