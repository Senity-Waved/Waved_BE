package com.senity.waved.base.security;

import com.senity.waved.base.jwt.TokenDto;
import com.senity.waved.base.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String userEmail = authentication.getName();
        TokenDto token = tokenProvider.createToken(userEmail);

        String url = makeRedirectUrl(token.getAccessToken());
        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private String makeRedirectUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_URI + "/login/google/token/" + token)
                .build().toUriString();
    }
}