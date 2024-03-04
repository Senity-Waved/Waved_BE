package com.senity.waved.base.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Refresh";
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest, AUTHORIZATION_HEADER);
        validateToken(jwt, "액세스 토큰", httpServletRequest.getRequestURI());

        String refreshToken = resolveToken(httpServletRequest, REFRESH_TOKEN_HEADER);
        validateToken(refreshToken, "리프레시 토큰", httpServletRequest.getRequestURI());

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void validateToken(String token, String tokenType, String requestURI) {
        if (StringUtils.hasText(token)) {
            try {
                if (tokenType.equals("액세스 토큰")) {
                    tokenProvider.validateAccessToken(token);
                    Authentication authentication = tokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}",
                            authentication.getName(), requestURI);
                } else {
                    tokenProvider.validateRefreshToken(token);
                }
            } catch (JwtException e) {
                logger.debug("유효한 JWT {}이 없습니다, uri: {}", tokenType, requestURI);
                throw new AuthenticationServiceException("유효한 JWT " + tokenType + "이 없습니다.", e);
            }
        } else {
            logger.debug("{}이 없습니다, uri: {}", tokenType, requestURI);
        }
    }

    private String resolveToken(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}