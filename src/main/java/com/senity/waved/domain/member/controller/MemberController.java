package com.senity.waved.domain.member.controller;

import com.senity.waved.base.jwt.TokenProvider;
import com.senity.waved.domain.member.dto.GithubInfoDto;
import com.senity.waved.domain.member.dto.request.MemberJoinRequestDto;
import com.senity.waved.domain.member.dto.ProfileEditDto;
import com.senity.waved.domain.member.dto.response.ProfileInfoResponseDto;
import com.senity.waved.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    @PatchMapping("/join")
    public ResponseEntity<String> join(
            @AuthenticationPrincipal User user,
            @RequestBody(required = false) MemberJoinRequestDto joinDto
    ) {
        memberService.joinAfterOauth(user.getUsername(), joinDto);
        return new ResponseEntity<>("회원가입 추가 정보 등록 성공했습니다.", HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(HttpServletRequest request) {
        String refreshToken = memberService.resolveRefreshToken(request.getHeader("Authorization"));
        return tokenProvider.generateAccessToken(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal User user, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        memberService.logout(user.getUsername(), token);
        return new ResponseEntity<>("로그아웃 완료했습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMember(@AuthenticationPrincipal User user) {
        memberService.deleteMember(user.getUsername());
        return new ResponseEntity<>("회원 탈퇴 완료했습니다.", HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ProfileInfoResponseDto memberProfile(@AuthenticationPrincipal User user) {
        return memberService.getProfileInfo(user.getUsername());
    }

    @GetMapping("/profile/edit")
    public ProfileEditDto getMemberProfileEdit(@AuthenticationPrincipal User user) {
        return memberService.getProfileInfoToEdit(user.getUsername());
    }

    // TODO 프로필 수정 PATCH

    @PostMapping("/github")
    public ResponseEntity<String> connectGithub(
            @AuthenticationPrincipal User user,
            @RequestBody GithubInfoDto github
    ) {
        memberService.checkGithubConnection(user.getUsername(), github);
        return new ResponseEntity<>("github 연동 완료했습니다.", HttpStatus.OK);
    }

    @GetMapping("/github")
    public GithubInfoDto getGithubInfo(@AuthenticationPrincipal User user) {
        return memberService.getGithubInfoToEdit(user.getUsername());
    }

    @DeleteMapping("/github")
    public ResponseEntity<String> disconnectGithub(@AuthenticationPrincipal User user) {
        memberService.deleteGithubInfo(user.getUsername());
        return new ResponseEntity<>("github 연동 해제했습니다.", HttpStatus.OK);
    }
}
