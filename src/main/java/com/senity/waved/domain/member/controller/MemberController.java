package com.senity.waved.domain.member.controller;

import com.senity.waved.base.jwt.TokenProvider;
import com.senity.waved.domain.member.dto.GithubInfoDto;
import com.senity.waved.domain.member.dto.ProfileEditDto;
import com.senity.waved.domain.member.dto.response.ProfileInfoResponseDto;
import com.senity.waved.domain.member.service.MemberService;
import com.senity.waved.domain.review.dto.response.ReviewResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    private OAuth2AuthorizedClientService authorizedClientService;
    private RestTemplate restTemplate;

    @PatchMapping("/edit")
    public ResponseEntity<String> editProfile(
            @AuthenticationPrincipal User user,
            @RequestBody(required = false) ProfileEditDto editDto
    ) {
        memberService.editMemberProfile(user.getUsername(), editDto);
        return new ResponseEntity<>("회원정보 등록 성공했습니다.", HttpStatus.OK);
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

    @GetMapping("/reviews")
    public Page<ReviewResponseDto> getReviews(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "limit", defaultValue = "5") int pageSize
    ) {
        return memberService.getReviewsPaged(user.getUsername(), pageNumber, pageSize);
    }
}