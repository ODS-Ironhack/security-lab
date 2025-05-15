package com.exer.security_lab.filters;

import com.exer.security_lab.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // pasa al siguiente filtro aunque la autenticación no sea correcta, puede que haya endpoints accesibles para todo el mundo.
            return;
        }

        String token = authHeader.substring(7);

        // validamos el token
        if (!jwtService.validateToken(token)) {
            filterChain.doFilter(request, response);// pasa al siguiente filtro
            return;
        }


        // EXTRAIGO TODA LA INFO DEL TOKEN relativa al usuario
        String username = jwtService.extractUsername(token);
        String rolesString = jwtService.extractRoles(token);

        // Convert the roles string to a list of Spring Security authorities
        Collection<GrantedAuthority> authorities = extractAuthorities(rolesString);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

        // Creamos la autenticación dentro del contexto de nuestra app
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private Collection<GrantedAuthority> extractAuthorities(String rolesString) {

        System.out.println(rolesString);
        // Process the roles string. Example: "[ROLE_ADMIN, ROLE_USER]"
        if (rolesString == null || rolesString.isEmpty()) {
            System.out.println("vacíos o mal los roles");
            return Collections.emptyList();
        }

        // Remove brackets and split by commas
        String roles = rolesString.replace("[", "").replace("]", "");
        String[] roleArray = roles.split(",");
        System.out.println(roleArray + " roleArray");

        return Arrays.stream(roleArray)
                .map(String::trim) // Remove spaces
                .map(SimpleGrantedAuthority::new) // We'll have roles in the correct format for Spring Security to recognize them
                .collect(Collectors.toList());
    }
}
