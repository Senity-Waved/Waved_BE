package com.senity.waved.domain.member.controller;

import com.senity.waved.base.jwt.TokenProvider;
import com.senity.waved.domain.member.dto.MemberJoinDto;
import com.senity.waved.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    @PatchMapping("/join")
    public ResponseEntity<String> join(Authentication authentication, @RequestBody MemberJoinDto joinDto) throws AccountNotFoundException {
        User user = (User) authentication.getPrincipal();
        memberService.joinAfterOauth(user, joinDto);
        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public String reissue(HttpServletRequest request) {
        String refreshToken = memberService.resolveRefreshToken(request.getHeader("Authorization"));
        return tokenProvider.generateAccessToken(refreshToken);
    }
}
