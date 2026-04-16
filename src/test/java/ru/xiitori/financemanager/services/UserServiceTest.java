package ru.xiitori.financemanager.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.xiitori.financemanager.exceptions.AuthorizationException;
import ru.xiitori.financemanager.mappers.UserMapper;
import ru.xiitori.financemanager.model.dto.auth.RegistrationDTO;
import ru.xiitori.financemanager.model.entity.User;
import ru.xiitori.financemanager.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private RegistrationDTO registrationDTO;
    private User userEntity;

    @BeforeEach
    void setUp() {
        registrationDTO = new RegistrationDTO("testuser", "password123", "password123");
        userEntity = new User();
        userEntity.setId(1L);
        userEntity.setUsername("testuser");
    }

    // Tests for register method - successful registration

    @Test
    void register_SuccessfulRegistration_ShouldSaveUser() {
        // Arrange
        when(userMapper.toEntity(registrationDTO)).thenReturn(userEntity);
        when(passwordEncoder.encode(registrationDTO.password())).thenReturn("encodedPassword");
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        // Act & Assert
        assertDoesNotThrow(() -> userService.register(registrationDTO));
        
        // Verify interactions
        verify(userMapper).toEntity(registrationDTO);
        verify(passwordEncoder).encode(registrationDTO.password());
        verify(userRepository).save(userEntity);
    }

    @Test
    void register_PasswordsDoNotMatch_ShouldThrowAuthorizationException() {
        // Arrange
        RegistrationDTO dtoWithDifferentPasswords = new RegistrationDTO("testuser", "password123", "differentPassword");

        // Act & Assert
        AuthorizationException exception = assertThrows(AuthorizationException.class, 
            () -> userService.register(dtoWithDifferentPasswords));
        
        assertEquals("Passwords do not match", exception.getMessage());
        
        // Verify that no further operations were performed
        verifyNoInteractions(userMapper);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(userRepository);
    }

    @Test
    void register_UsernameAlreadyExists_ShouldThrowAuthorizationException() {
        // Arrange
        when(userMapper.toEntity(registrationDTO)).thenReturn(userEntity);
        when(passwordEncoder.encode(registrationDTO.password())).thenReturn("encodedPassword");
        when(userRepository.save(userEntity)).thenThrow(new DataIntegrityViolationException("Duplicate username"));

        // Act & Assert
        AuthorizationException exception = assertThrows(AuthorizationException.class, 
            () -> userService.register(registrationDTO));
        
        assertEquals("User with username 'testuser' already exists", exception.getMessage());
        
        // Verify all expected interactions occurred
        verify(userMapper).toEntity(registrationDTO);
        verify(passwordEncoder).encode(registrationDTO.password());
        verify(userRepository).save(userEntity);
    }

    // Tests for loadUserByUsername method

    @Test
    void loadUserByUsername_UserExists_ShouldReturnUserDetails() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        var result = userService.loadUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ShouldThrowUsernameNotFoundException() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.loadUserByUsername(username));
        
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByUsername(username);
    }
}