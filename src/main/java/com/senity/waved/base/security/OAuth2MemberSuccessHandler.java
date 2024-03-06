package com.senity.waved.base.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senity.waved.base.jwt.TokenDto;
import com.senity.waved.base.jwt.TokenProvider;
import com.senity.waved.base.redis.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String userEmail = oAuth2User.getAttribute("email");

        TokenDto token = new TokenDto(tokenProvider.createAccessToken(userEmail),
                tokenProvider.createRefreshToken(userEmail));
        redisUtil.save(userEmail, token.getRefreshToken());

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(new ObjectMapper().writeValueAsString(token));
        out.flush();
    }
}