package com.pjqe.app.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    // PROBLEMA AQUI
    @Value("${api.key}")
    private final String apiKey;

    public ApiKeyAuthenticationFilter(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestApiKey = request.getHeader("X-API-Key");

        // Verifica se a chave da requisição é igual à chave configurada na aplicação
        if (apiKey.equals(requestApiKey)) {
            // Chave válida. Cria um objeto de autenticação.
            // ATUALIZADO: Adiciona a autoridade SUPERADMIN para permitir o acesso aos endpoints de admin.
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("SUPERADMIN"));
            var authentication = new UsernamePasswordAuthenticationToken(
                    "api-admin-user", // Um principal genérico para o admin
                    null,
                    authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
