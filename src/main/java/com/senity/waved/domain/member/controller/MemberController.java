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
@RequestMapping("/api/v1/members")
public class MemberController {

    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    @PatchMapping("/join")
    public ResponseEntity<String> join(Authentication authentication, @RequestBody(required = false) MemberJoinDto joinDto) throws AccountNotFoundException {
        User user = (User) authentication.getPrincipal();
        memberService.joinAfterOauth(user, joinDto);
        return new ResponseEntity<>("회원가입 추가 정보 등록 성공", HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(HttpServletRequest request) {
        String refreshToken = memberService.resolveRefreshToken(request.getHeader("Authorization"));
        return new ResponseEntity<>(tokenProvider.generateAccessToken(refreshToken), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        User user = (User) authentication.getPrincipal();
        String email = user.getUsername();
        memberService.logout(token, email);
        return new ResponseEntity<>("로그아웃 완료", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMember(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String email = user.getUsername();
        memberService.deleteMember(email);
        return new ResponseEntity<>("회원 탈퇴 완료", HttpStatus.OK);
    }
}
