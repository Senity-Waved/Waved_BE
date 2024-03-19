package com.senity.waved.domain.verification.controller;

import com.senity.waved.domain.quiz.dto.response.QuizResponseDto;
import com.senity.waved.domain.quiz.entity.Quiz;
import com.senity.waved.domain.quiz.service.QuizService;
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
    private final QuizService quizService;

    @GetMapping("/{challengeGroupId}")
    public ResponseEntity<QuizResponseDto> getTodaysQuiz(@PathVariable("challengeGroupId") Long challengeGroupId) {
        Quiz quiz = quizService.findValidQuizByChallengeGroupId(challengeGroupId);

        QuizResponseDto quizResponseDto = new QuizResponseDto(quiz);
        return ResponseEntity.ok().body(quizResponseDto);
    }

    @PostMapping("/{challengeGroupId}")
    public ResponseEntity<String> verifyChallenge(@AuthenticationPrincipal User user,
                                                  @PathVariable("challengeGroupId") Long challengeGroupId,
                                                  @ModelAttribute VerificationRequestDto requestDto) {

        verificationService.verifyChallenge(requestDto, user.getUsername(), challengeGroupId);
        return ResponseEntity.ok("인증이 성공적으로 제출되었습니다.");
    }

}
