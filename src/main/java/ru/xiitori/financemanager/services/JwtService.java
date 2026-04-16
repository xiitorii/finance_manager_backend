package ru.xiitori.financemanager.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.xiitori.financemanager.exceptions.AuthorizationException;
import ru.xiitori.financemanager.model.entity.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService implements JwtBaseService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long lifetime;

    @Override
    public String generateToken(Authentication authentication) {
        var user = (User) authentication.getPrincipal();

        if (user == null) {
            throw new AuthorizationException("User not authorized");
        }

        var claims = Map.of("permissions", user.getAuthorities());

        return Jwts.builder()
                .subject(user.getUsername())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + lifetime))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            getClaim(token, "sub", String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return getClaim(token, "sub", String.class);
    }

    @Override
    public <T> T getClaim(String token, String claimName, Class<T> type) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(claimName, type);
    }

    public SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
