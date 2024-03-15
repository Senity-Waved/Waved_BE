package com.senity.waved.base.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senity.waved.base.jwt.TokenDto;
import com.senity.waved.base.jwt.TokenProvider;
import com.senity.waved.base.redis.Redis;
import com.senity.waved.base.redis.RedisUtil;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.member.exception.MemberNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String userEmail = oAuth2User.getAttribute("email");
        Member member = getMemberByEmail(userEmail);

        TokenDto token = new TokenDto(tokenProvider.createAccessToken(userEmail),
                tokenProvider.createRefreshToken(userEmail), member.getHasInfo());

        Optional<Redis> optionalRedis = redisUtil.findByEmail(userEmail);
        if (optionalRedis.isPresent()) {
            redisUtil.deleteByEmail(userEmail);
        }
        redisUtil.save(userEmail, token.getRefreshToken());
        log.info("info log={}", userEmail + token.getRefreshToken());

        response.setHeader("Authorization", "Bearer " + token.getAccessToken());

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(new ObjectMapper().writeValueAsString(token));
        out.flush();
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.getMemberByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("해당 멤버를 찾을 수 없습니다."));
    }
}