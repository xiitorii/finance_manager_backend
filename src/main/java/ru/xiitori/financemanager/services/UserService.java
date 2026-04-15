package ru.xiitori.financemanager.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.xiitori.financemanager.exceptions.AuthorizationException;
import ru.xiitori.financemanager.mappers.UserMapper;
import ru.xiitori.financemanager.model.dto.auth.RegistrationDTO;
import ru.xiitori.financemanager.repositories.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void register(RegistrationDTO dto) {
        validatePasswordMatch(dto.password(), dto.repeatPassword());

        var user = mapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AuthorizationException("User with username '" + dto.username() + "' already exists");
        }
    }

    private void validatePasswordMatch(String password, String repeatPassword) {
        if (!password.equals(repeatPassword)) {
            throw new AuthorizationException("Passwords do not match");
        }
    }
}
