package com.pjqe.app.config;

import com.pjqe.app.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JpaUserDetailsService jpaUserDetailsService;
    private final AppCredentials appCredentials;

    public SecurityConfig(JpaUserDetailsService jpaUserDetailsService, AppCredentials appCredentials) {
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.appCredentials = appCredentials;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        ApiKeyAuthenticationFilter apiKeyFilter = new ApiKeyAuthenticationFilter(appCredentials.getApiKey());

        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/clients/register").permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("SUPERADMIN")
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        // Regras explícitas para SUPERADMIN
                        .requestMatchers(
                                "/admin/add-system-user",
                                "/admin/delete-system-user/**",
                                "/admin/edit-system-user/**",
                                "/admin/update-system-user/**",
                                "/admin/credentials/add",      // <-- Adicionado explicitamente
                                "/admin/credentials/update"    // <-- Adicionado explicitamente
                        ).hasAuthority("SUPERADMIN")
                        // Regra geral para o painel de admin
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN", "SUPERADMIN")
                        // Regra para usuários comuns
                        .requestMatchers("/").hasAnyAuthority("USER", "ADMIN", "SUPERADMIN")
                        // Rotas públicas
                        .requestMatchers("/login", "/register").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
