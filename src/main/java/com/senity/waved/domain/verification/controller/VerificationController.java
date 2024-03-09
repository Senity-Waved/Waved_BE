package com.senity.waved.domain.verification.controller;

import com.senity.waved.domain.verification.dto.request.VerificationRequestDto;
import com.senity.waved.domain.verification.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/verify")
public class VerificationController {

    private final VerificationService verificationService;

    @PostMapping
    public ResponseEntity<String> verifyChallenge(@AuthenticationPrincipal User user,
                                             @RequestBody VerificationRequestDto requestDto) {

        verificationService.verifyChallenge(requestDto, user.getUsername());
        return ResponseEntity.ok("인증이 성공적으로 제출되었습니다.");
    }

}
