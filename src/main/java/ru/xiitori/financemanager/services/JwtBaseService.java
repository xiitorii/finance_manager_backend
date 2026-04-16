package ru.xiitori.financemanager.services;

import org.springframework.security.core.Authentication;

public interface JwtBaseService {
    String generateToken(Authentication authentication);

    boolean validateToken(String token);

    <T> T getClaim(String token, String claimName, Class<T> type);

    String extractUsername(String token);
}
