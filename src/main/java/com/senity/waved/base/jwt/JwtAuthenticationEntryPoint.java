package com.senity.waved.base.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        if (authException instanceof AuthenticationServiceException) {
            AuthenticationServiceException jwtException = (AuthenticationServiceException) authException;
            // JWT 예외 처리
            setResponse(response, 1, jwtException.getMessage());
        } else {
            // 그 외 인증 실패 처리
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void setResponse(HttpServletResponse response, int code, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"message\" : \"" + msg
                + "\", \"code\" : \"" + code
                + "\", \"status\" : " + "401"
                + ", \"errors\" : [ ] }");
    }
}

