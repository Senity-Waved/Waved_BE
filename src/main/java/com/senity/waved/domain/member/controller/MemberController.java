package com.senity.waved.domain.member.controller;

import com.senity.waved.base.jwt.TokenProvider;
import com.senity.waved.common.ResponseDto;
import com.senity.waved.domain.member.dto.GithubInfoDto;
import com.senity.waved.domain.member.dto.ProfileEditDto;
import com.senity.waved.domain.member.dto.response.ProfileInfoResponseDto;
import com.senity.waved.domain.member.service.MemberService;
import com.senity.waved.domain.paymentRecord.dto.response.PaymentRecordResponseDto;
import com.senity.waved.domain.review.dto.response.MemberReviewResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    @PatchMapping("/edit")
    public ResponseEntity<ResponseDto> editProfile(
            @AuthenticationPrincipal User user,
            @RequestBody(required = false) ProfileEditDto editDto
    ) {
        memberService.editMemberProfile(user.getUsername(), editDto);
        return ResponseDto.of(HttpStatus.OK, "회원정보 등록 성공했습니다.");
    }

    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(HttpServletRequest request) {
        String refreshToken = memberService.resolveRefreshToken(request.getHeader("Authorization"));
        return tokenProvider.generateAccessToken(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@AuthenticationPrincipal User user, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        memberService.logout(user.getUsername(), token);
        return ResponseDto.of(HttpStatus.OK, "로그아웃 완료했습니다.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteMember(@AuthenticationPrincipal User user) {
        memberService.deleteMember(user.getUsername());
        return ResponseDto.of(HttpStatus.OK, "회원 탈퇴 완료했습니다.");
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
    public ResponseEntity<ResponseDto> connectGithub(
            @AuthenticationPrincipal User user,
            @RequestBody GithubInfoDto github
    ) {
        memberService.checkGithubConnection(user.getUsername(), github);
        return ResponseDto.of(HttpStatus.OK, "github 연동 완료했습니다.");
    }

    @GetMapping("/github")
    public GithubInfoDto getGithubInfo(@AuthenticationPrincipal User user) {
        return memberService.getGithubInfoToEdit(user.getUsername());
    }

    @DeleteMapping("/github")
    public ResponseEntity<ResponseDto> disconnectGithub(@AuthenticationPrincipal User user) {
        memberService.deleteGithubInfo(user.getUsername());
        return ResponseDto.of(HttpStatus.OK, "github 연동 해제했습니다.");
    }

    @GetMapping("/reviews")
    public Page<MemberReviewResponseDto> getReviews(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "limit", defaultValue = "5") int pageSize
    ) {
        return memberService.getReviewsPaged(user.getUsername(), pageNumber, pageSize);
    }

    @GetMapping("/paymentRecords")
    public Page<PaymentRecordResponseDto> getMyPaymentRecords(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "limit", defaultValue = "5") int pageSize
    ) {
        return memberService.getMyPaymentRecordsPaged(user.getUsername(), pageNumber, pageSize);
    }
}

