package com.senity.waved.domain.myChallenge.controller;

import com.senity.waved.domain.myChallenge.dto.response.MyChallengeResponseDto;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.myChallenge.service.MyChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/myChallenges")
public class MyChallengeController {

    private final MyChallengeService myChallengeService;

    @GetMapping("/{myChallengeId}")
    public ResponseEntity<MyChallengeResponseDto> getMyChallengeStatus(@PathVariable("myChallengeId") Long myChallengeId) {
        MyChallenge myChallenge = myChallengeService.findMyChallengeById(myChallengeId);
        MyChallengeResponseDto myChallengeResponseDto = new MyChallengeResponseDto(myChallenge);
        return ResponseEntity.ok().body(myChallengeResponseDto);
    }

}
