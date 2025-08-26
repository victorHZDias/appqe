package com.pjqe.app.service;

import com.pjqe.app.entity.SystemUser;
import com.pjqe.app.repository.SystemUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class SystemUserService {

    private final SystemUserRepository systemUserRepository;
    private final PasswordEncoder passwordEncoder;

    public SystemUserService(SystemUserRepository systemUserRepository, PasswordEncoder passwordEncoder) {
        this.systemUserRepository = systemUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public SystemUser registerSystemUser(String username, String password, String role) {
        SystemUser newUser = new SystemUser();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(role);
        return systemUserRepository.save(newUser);
    }

    // NOVO MÉTODO para atualizar um usuário
    @Transactional
    public void updateSystemUser(Long id, SystemUser updatedUserData) {
        SystemUser existingUser = systemUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        existingUser.setUsername(updatedUserData.getUsername());
        existingUser.setRole(updatedUserData.getRole());

        // Apenas atualiza a senha se uma nova foi fornecida
        if (StringUtils.hasText(updatedUserData.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updatedUserData.getPassword()));
        }

        systemUserRepository.save(existingUser);
    }
}
