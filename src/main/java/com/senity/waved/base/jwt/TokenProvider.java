package com.senity.waved.base.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private final String secret2;
    private final long tokenValidityInMilliseconds2;
    private Key accessTokenKey;
    private Key refreshTokenKey;

    public TokenProvider(
            @Value("${custom.jwt.secret}") String secret,
            @Value("${custom.jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            @Value("${custom.jwt.secret2}") String secret2,
            @Value("${custom.jwt.token-validity-in-seconds2}") long tokenValidityInSeconds2)

    {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.secret2 = secret2;
        this.tokenValidityInMilliseconds2 = tokenValidityInSeconds2 * 2000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] accessTokenKeyBytes = Decoders.BASE64.decode(secret);
        this.accessTokenKey = Keys.hmacShaKeyFor(accessTokenKeyBytes);
        byte[] refreshTokenKeyBytes = Decoders.BASE64.decode(secret2);
        this.refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenKeyBytes);
    }

    public TokenDto createToken(String email) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);
        Date validity2 = new Date(now + this.tokenValidityInMilliseconds2);

        String accessToken = Jwts.builder()
                .setSubject(email)
                .signWith(accessTokenKey, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .signWith(refreshTokenKey, SignatureAlgorithm.HS512)
                .setExpiration(validity2)
                .compact();

        return new TokenDto(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(accessTokenKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);

    }

    public void validateAccessToken(String token) {
        validateToken(token, accessTokenKey);
    }

    public void validateRefreshToken(String token) {
        validateToken(token, refreshTokenKey);
    }

    private void validateToken(String token, Key key) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new MalformedJwtException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT 토큰이 잘못되었습니다.");
        }
    }
}
