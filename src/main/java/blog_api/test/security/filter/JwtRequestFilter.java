package blog_api.test.security.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import blog_api.test.security.jwt.JwtUtil;
import blog_api.test.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter { // Ensures that this filter runs only once per HTTP request
  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Autowired
  private JwtUtil jwtUtil;

  /**
   * This is the core method where the filtering logic resides.
   * 
   * - Extract Header: It attempts to get the "Authorization" header from the
   * incoming request.
   * 
   * - Extract JWT: If the header exists and starts with "Bearer ", it extracts the
   * actual JWT string.
   * 
   * - Extract Username: It uses "jwtUtil.extractUsername(jwt)" to get the username from the token.
   * 
   * - Check Authentication status: It verifies that a username was succesfully extracted AND
   * that the "SecurityContextHolder" does not already have an authentication object set.
   * 
   * - Load User Details: It uses "userDetailsService.loadUserByUsername(username)" to load
   * the full "UserDetails" object.
   * 
   * - Validate Token: It calls "jwtUtil.validateToken(jwt, userDetails.getUsername())" to ensure
   * the token is still valid.
   * 
   * - Set Authentication: If the token is valid, it constructs a "UsernamePasswordAuthenticationToken" and sets
   * it in the "SecurityContextHolder".
   * 
   * - "chain.doFilter(request, response)": It passes the reqiest and response objects to the
   * next filter in the chain.
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    final String authorizationHeader = request.getHeader("Authorization");

    String username = null;
    String jwt = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
      try {
        username = jwtUtil.extractUsername(jwt);
      } catch (Exception e) {
        System.err.println("Error extracting username from JWT: " + e.getMessage());
      }
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

      if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }

    chain.doFilter(request, response);
  }
}
