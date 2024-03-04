package com.senity.waved.domain.member.controller;

import com.senity.waved.base.jwt.TokenProvider;
import com.senity.waved.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    @PostMapping("/reissue")
    public String reissue(HttpServletRequest request) {
        String refreshToken = memberService.resolveRefreshToken(request.getHeader("Authorization"));
        return tokenProvider.generateAccessToken(refreshToken);
    }
}
