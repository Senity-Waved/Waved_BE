package com.senity.waved.domain.myChallenge.controller;

import com.senity.waved.common.ResponseDto;
import com.senity.waved.domain.myChallenge.dto.response.MyChallengeResponseDto;
import com.senity.waved.domain.myChallenge.dto.response.MyVerifsResponseDto;
import com.senity.waved.domain.myChallenge.entity.ChallengeStatus;
import com.senity.waved.domain.myChallenge.service.MyChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/myChallenges")
public class MyChallengeController {

    public final MyChallengeService myChallengeService;

    @GetMapping
    public List<MyChallengeResponseDto> getMyChallenges(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "status", defaultValue = "PROGRESS") ChallengeStatus status
    ) {
        return myChallengeService.getMyChallengesListed(user.getUsername(), status);
    }
 
    @GetMapping("/{myChallengeId}")
    public MyVerifsResponseDto getMyChallengeStatus(@PathVariable("myChallengeId") Long myChallengeId) {
        return myChallengeService.getMyVerifications(myChallengeId);
    }

    @DeleteMapping("/{myChallengeId}/delete")
    public ResponseEntity<ResponseDto> cancelMyChallenge(
            @PathVariable("myChallengeId") Long myChallengeId,
            @AuthenticationPrincipal User user
    ) {
        myChallengeService.cancelAppliedMyChallenge(user.getUsername(), myChallengeId);
        return ResponseDto.of(HttpStatus.OK, "챌린지 그룹 신청 취소 완료했습니다.");
    }
}
