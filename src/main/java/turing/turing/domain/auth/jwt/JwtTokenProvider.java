package turing.turing.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.sql.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import turing.turing.domain.Role;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private static String SECRET_KEY;
    @Value("${jwt.access-expiration}")
    private static long EXPIRATION_TIME;
    @Value("${jwt.refresh-expiration}")
    private static long REFRESH_EXPIRATION_TIME;

    public String createAccessToken(String email, Long memberId, Role role) {
        Claims claims = Jwts.claims()
                .add("memberId", memberId)
                .add("role", role)
                .build();

        return Jwts.builder()
                .subject(email)
                .claims(claims)
                .signWith(getSigningKey())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    //TODO Redis
    public String createRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .signWith(getSigningKey())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .compact();
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

            return claims.getPayload().get("role", Role.class);
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

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
