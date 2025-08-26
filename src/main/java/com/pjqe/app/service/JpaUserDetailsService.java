package com.pjqe.app.service;

import com.pjqe.app.entity.Client;
import com.pjqe.app.entity.SystemUser;
import com.pjqe.app.repository.ClientRepository;
import com.pjqe.app.repository.SystemUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final SystemUserRepository systemUserRepository;

    public JpaUserDetailsService(ClientRepository clientRepository, SystemUserRepository systemUserRepository) {
        this.clientRepository = clientRepository;
        this.systemUserRepository = systemUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Tenta encontrar como um Usuário do Sistema (Admin, Superadmin)
        Optional<SystemUser> systemUserOpt = systemUserRepository.findByUsername(username);
        if (systemUserOpt.isPresent()) {
            SystemUser systemUser = systemUserOpt.get();
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(systemUser.getRole()));
            return new User(systemUser.getUsername(), systemUser.getPassword(), authorities);
        }

        // 2. Se não for um Usuário do Sistema, tenta encontrar como um Cliente
        Optional<Client> clientOpt = clientRepository.findByUsername(username);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            // Clientes recebem a role "USER" por padrão
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
            return new User(client.getUsername(), client.getPassword(), authorities);
        }

        // 3. Se não encontrar em nenhuma das tabelas, lança a exceção
        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
}
