package ru.xiitori.financemanager.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.xiitori.financemanager.model.dto.auth.RegistrationDTO;
import ru.xiitori.financemanager.model.entity.User;
import ru.xiitori.financemanager.repositories.UserRepository;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void register(RegistrationDTO dto) throws AccessDeniedException {
        //TODO
        var newUser = new User();

        newUser.setUsername(dto.username());

        if (!dto.password().equals(dto.repeatPassword())) {
            throw new AccessDeniedException("Passwords don't match!");
        }

        newUser.setPassword(passwordEncoder.encode(dto.password()));

        userRepository.save(newUser);
    }
}
