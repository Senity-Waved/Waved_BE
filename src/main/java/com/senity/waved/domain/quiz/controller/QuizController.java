package com.senity.waved.domain.quiz.controller;

import com.senity.waved.domain.quiz.dto.response.QuizResponseDto;
import com.senity.waved.domain.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quiz")
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/{challengeGroupId}")
    public ResponseEntity<QuizResponseDto> getTodaysQuiz(@PathVariable("challengeGroupId") Long challengeGroupId) {
        QuizResponseDto quizResponseDto = quizService.getTodaysQuiz(challengeGroupId);
        return ResponseEntity.ok().body(quizResponseDto);
    }

    @GetMapping("/{challengeGroupId}/dates")
    public ResponseEntity<QuizResponseDto> getQuizByDate(
            @PathVariable("challengeGroupId") Long challengeGroupId,
            @RequestParam("quizDate") Timestamp quizDate) {

        ZonedDateTime requestQuizDate = quizDate.toInstant().atZone(ZoneId.systemDefault());
        QuizResponseDto quizResponseDto = quizService.getQuizByDate(challengeGroupId, requestQuizDate);
        return ResponseEntity.ok().body(quizResponseDto);
    }
}
