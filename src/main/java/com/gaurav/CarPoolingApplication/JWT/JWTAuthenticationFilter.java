package com.gaurav.CarPoolingApplication.JWT;

import com.gaurav.CarPoolingApplication.Service.UserEntityService.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component @Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;
    public JWTAuthenticationFilter(
            JWTUtils jwtUtils,
            CustomUserDetailsService customUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        String requestPath = request.getServletPath();
        if(requestPath.startsWith("/auth") ||
                requestPath.startsWith("/v3/api-docs")
                || requestPath.startsWith("/swagger-ui")
                || requestPath.startsWith("/swagger-ui.html")) {
            filterChain.doFilter(request,response);
            return;
        }
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            String jwtToken = authorizationHeader.substring(7);
            try {
                String username = this.jwtUtils.getUsername(jwtToken);
                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
                    if (jwtUtils.validateToken(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }
            catch (Exception exception) {
                log.warn("Authentication Failed {}", exception.getLocalizedMessage());
                throw new RuntimeException("Authentication Failed " + exception);
            }
        }
    }
}
