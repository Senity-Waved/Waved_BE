package com.senity.waved.base.jwt;

import com.senity.waved.domain.member.exception.MultipleLoginException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

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
                writeErrorResponse(servletResponse, e.getMessage());
                return;
            } catch (BlackListedTokenException e) {
                writeErrorResponse(servletResponse, e.getMessage());
                return;
            } catch (MalformedJwtException e) {
                writeErrorResponse(servletResponse, e.getMessage());
                return;
            } catch (MultipleLoginException e) {
                writeErrorResponse(servletResponse, e.getMessage());
                return;
            } catch (UnsupportedJwtException e) {
                writeErrorResponse(servletResponse, e.getMessage());
                return;
            } catch (IllegalArgumentException e) {
                writeErrorResponse(servletResponse, e.getMessage());
                return;
            }
        } else {
            writeErrorResponse(servletResponse, "인증정보가 없습니다.");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void writeErrorResponse(ServletResponse response, String message) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("text/plain; charset=UTF-8");
        httpServletResponse.getWriter().write(message);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}