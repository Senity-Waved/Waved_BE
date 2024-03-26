package com.senity.waved.base.jwt;

import com.senity.waved.base.redis.Redis;
import com.senity.waved.base.redis.RedisUtil;
import com.senity.waved.domain.member.exception.MultipleLoginException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class TokenProvider implements InitializingBean {

    private final String secret;
    private final long tokenValidityInMilliseconds;
    private final long tokenValidityInMilliseconds2;
    private Key key;
    private final RedisUtil redisUtil;

    public TokenProvider(
            @Value("${custom.jwt.secret}") String secret,
            @Value("${custom.jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            @Value("${custom.jwt.token-validity-in-seconds2}") long tokenValidityInSeconds2,
            RedisUtil redisUtil)
    {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.tokenValidityInMilliseconds2 = tokenValidityInSeconds2 * 2000;
        this.redisUtil = redisUtil;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String email) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        String accessToken = Jwts.builder()
                .setSubject(email)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();

        return accessToken;
    }

    public String createRefreshToken(String email) {
        long now = (new Date()).getTime();
        Date validity2 = new Date(now + this.tokenValidityInMilliseconds2);

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity2)
                .compact();

        return refreshToken;
    }

    public ResponseEntity<String> generateAccessToken(String refreshToken) {
        try {
            validateToken(refreshToken);
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();
            String email = claims.getSubject();
            Optional<Redis> optionalRedis = redisUtil.findByEmail(email);
            if (optionalRedis.isPresent() && (!optionalRedis.get().getRefreshToken().equals(refreshToken))) {
                throw new MultipleLoginException("다른 위치에서 로그인하여 현재 세션이 로그아웃되었습니다.");
            }

            String accessToken = createAccessToken(email);
            return ResponseEntity.ok(accessToken);
        } catch (MultipleLoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
        public Authentication getAuthentication(String token) {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            Collection<SimpleGrantedAuthority> authorities = Arrays.stream(new String[]{"ROLE_USER"})
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            User principal = new User(username, "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, token, authorities);
        }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();

        List<String> adminMembers = Arrays.asList(
                "waved7777@gmail.com", "imholy96@gmail.com",
                "vywns9978@gmail.com", "waved8888@gmail.com", "fetest1228@gmail.com"
        );

        Collection<SimpleGrantedAuthority> authorities;
        if (adminMembers.contains(username)) {
            authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        User principal = new User(username, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public void validateToken(String token) {
        if (redisUtil.hasKeyBlackList(token)) {
            throw new BlackListedTokenException("블랙리스트에 포함된 토큰입니다.");
        }
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
