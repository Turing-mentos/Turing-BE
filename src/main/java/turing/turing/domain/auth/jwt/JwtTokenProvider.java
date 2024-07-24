package turing.turing.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.sql.Date;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import turing.turing.domain.member.Provider;
import turing.turing.domain.member.Role;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RedisTemplate<String, String> redisTemplate;

    private static String SECRET_KEY;
    private static long EXPIRATION_TIME;
    private static long REFRESH_EXPIRATION_TIME;

    @Value("${jwt.secret}")
    public void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    @Value("${jwt.access-expiration}")
    public void setExpirationTime(long expirationTime) {
        EXPIRATION_TIME = expirationTime;
    }

    @Value("${jwt.refresh-expiration}")
    public void setRefreshExpirationTime(long refreshExpirationTime) {
        REFRESH_EXPIRATION_TIME = refreshExpirationTime;
    }

    public String createAccessToken(String email, Long memberId, Role role, Provider provider) {
        Claims claims = Jwts.claims()
                .add("memberId", memberId)
                .add("role", role)
                .add("provider", provider)
                .build();

        return Jwts.builder()
                .subject(email)
                .claims(claims)
                .signWith(getSigningKey())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    public String createRefreshToken(String email, Long memberId, Role role, Provider provider) {
        Claims claims = Jwts.claims()
                .add("memberId", memberId)
                .add("role", role)
                .add("provider", provider)
                .build();

        String refreshToken = Jwts.builder()
                .subject(email)
                .claims(claims)
                .signWith(getSigningKey())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .compact();

        String redisId = createRedisId(memberId, role);
        redisTemplate.opsForValue().set(redisId, refreshToken, REFRESH_EXPIRATION_TIME, TimeUnit.MILLISECONDS);

        return refreshToken;
    }

    public String getEmailFromToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return claims.getPayload().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public Role getRoleFromToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return Role.valueOf(claims.getPayload().get("role", String.class));
        } catch (ExpiredJwtException e) {
            throw new JwtException("Expired token");
        } catch (JwtException e) {
            throw new JwtException("Invalid token");
        }
    }

    public Long getMemberIdFromToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return claims.getPayload().get("memberId", Long.class);
        } catch (ExpiredJwtException e) {
            throw new JwtException("Expired token");
        } catch (JwtException e) {
            throw new JwtException("Invalid token");
        }
    }

    public Provider getProviderFromToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return Provider.valueOf(claims.getPayload().get("provider", String.class));
        } catch (ExpiredJwtException e) {
            throw new JwtException("Expired token");
        } catch (JwtException e) {
            throw new JwtException("Invalid token");
        }
    }

    public boolean validationToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return !claims.getPayload().getExpiration().before(new Date(System.currentTimeMillis()));
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean validationRefreshToken(String token) {
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);

        Long memberId = claims.getPayload().get("memberId", Long.class);
        Role role = Role.valueOf(claims.getPayload().get("role", String.class));
        String redisId = createRedisId(memberId, role);

        String savedToken = redisTemplate.opsForValue().get(redisId);

        return token.equals(savedToken);
    }

    public void deleteRefreshToken(String redisId) {
        redisTemplate.delete(redisId);
    }

    public String createRedisId(Long memberId, Role role) {
        if (role == Role.TEACHER) {
            return "T_" + memberId;
        }
        return "S_" + memberId;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}