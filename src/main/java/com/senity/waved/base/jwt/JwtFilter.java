package com.senity.waved.base.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt)) {
            try {
                tokenProvider.validateToken(jwt);
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}",
                        authentication.getName(), requestURI);
            } catch (ExpiredJwtException e) {
                setTokenExceptionAttribute(servletRequest, "만료된 JWT 토큰입니다.", requestURI);
            } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
                setTokenExceptionAttribute(servletRequest, "잘못된 JWT 토큰입니다.", requestURI);
            }
        } else {
            setTokenExceptionAttribute(servletRequest, "인증정보가 없습니다.", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void setTokenExceptionAttribute(ServletRequest servletRequest, String message, String requestURI) {
        Map<String, String> map = new HashMap<>();
        map.put("result", "FAIL");
        map.put("msg", message);
        servletRequest.setAttribute("tokenException", map);
        logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
    }
}