package com.senity.waved.base.security;

import com.senity.waved.base.jwt.TokenDto;
import com.senity.waved.base.jwt.TokenProvider;
import com.senity.waved.base.redis.Redis;
import com.senity.waved.base.redis.RedisRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${custom.site.baseUrl}")
    private String REDIRECT_URI;
    private final TokenProvider tokenProvider;
    private final RedisRepository redisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String userEmail = (String) oAuth2User.getAttribute("email");
        TokenDto token = tokenProvider.createToken(userEmail);

        redisRepository.save(new Redis(userEmail, token.getRefreshToken()));

        String url = makeRedirectUrl(token.getAccessToken());
        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private String makeRedirectUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_URI + "/login/google/token/" + token)
                .build().toUriString();
    }
}