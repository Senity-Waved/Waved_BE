package com.senity.waved.domain.member.controller;

import com.senity.waved.base.jwt.TokenDto;
import com.senity.waved.base.jwt.TokenProvider;
import com.senity.waved.domain.member.dto.GithubInfoDto;
import com.senity.waved.domain.member.dto.ProfileEditDto;
import com.senity.waved.domain.member.dto.response.ProfileInfoResponseDto;
import com.senity.waved.domain.member.entity.AuthLevel;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.service.MemberService;
import com.senity.waved.domain.review.dto.response.ReviewResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;


import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("/login")
    public TokenDto getTokens(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        // 액세스 토큰을 사용하여 Google API 호출을 위한 HTTP 요청을 생성합니다.
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getTokenValue());
        RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create("https://www.googleapis.com/oauth2/v3/userinfo"));

        // Google API로부터 사용자 정보를 가져옵니다.
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        String user = "";
        //return memberService.getNewTokens(user.getUsername());
        return memberService.getNewTokens(user);
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUserInfo(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {
        // Google OAuth 2.0로부터 액세스 토큰을 가져옵니다.
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        // 액세스 토큰을 사용하여 Google API 호출을 위한 HTTP 요청을 생성합니다.
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getTokenValue());
        RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create("https://www.googleapis.com/oauth2/v3/userinfo"));

        // Google API로부터 사용자 정보를 가져옵니다.
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        return response;
    }
}