
/*package com.ecobazaar.ecobazaar.security;


import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecobazaar.ecobazaar.util.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // ⚠️ FIX: EXPLICITLY SKIP PUBLIC ENDPOINTS 
        // This is necessary because the filter runs *before* Spring's default security rules.
        String path = request.getRequestURI();
        if (path.contains("/api/auth/login") || path.contains("/api/auth/register")) {
            filterChain.doFilter(request, response);
            return; // CRITICAL: Stop execution here for public paths
        }
        // ----------------------------------------------------

        String header = request.getHeader("Authorization");

        // If no token, continue filter chain (Spring Security will handle the 403 on protected paths)
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        // Validate token
        if (!jwtUtil.validateToken(token)) {
            // Token is invalid, let the request continue to be blocked by Spring Security
            filterChain.doFilter(request, response);
            return; 
        }

        // Extract claims (email + role)
        Claims claims = jwtUtil.getClaims(token);
        String email = claims.getSubject();
        String role = claims.get("role", String.class);
        
        // Create Spring Authentication object
        var authority = new SimpleGrantedAuthority(role);
        var auth = new UsernamePasswordAuthenticationToken(email, null, Collections.singletonList(authority));

        // Save authentication in context
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Continue request
        filterChain.doFilter(request, response);
    }
}*/
package com.ecobazaar.ecobazaar.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecobazaar.ecobazaar.util.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = jwtUtil.getClaims(token);
        String email = claims.getSubject();
        String role = claims.get("role", String.class);
        Long userId = claims.get("userId", Long.class); 

        var authority = new SimpleGrantedAuthority(role);
        var auth = new UsernamePasswordAuthenticationToken(email, null, Collections.singletonList(authority));

       
        auth.setDetails(userId);

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }
}
