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
    private Key key;

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
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createToken(String email) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);
        Date validity2 = new Date(now + this.tokenValidityInMilliseconds2);

        String accessToken = Jwts.builder()
                .setSubject(email)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity2)
                .compact();

        return new TokenDto(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
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

    public Map<String, String> validateToken(String token) {
        Map<String, String> map = new HashMap<>();

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            map.put("result", "SUCCESS");
            map.put("msg", "인증성공");
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            map.put("result", "FAIL");
            map.put("msg", "잘못된 JWT 서명입니다");
        } catch (ExpiredJwtException e) {
            map.put("result", "FAIL");
            map.put("msg", "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            map.put("result", "FAIL");
            map.put("msg", "지원되지 않는 JWT 토큰입니다");
        } catch (IllegalArgumentException e) {
            map.put("result", "FAIL");
            map.put("msg", "JWT 토큰이 잘못되었습니다.");
        }
        return map;
    }
}