package com.senity.waved.domain.quiz.controller;

import com.senity.waved.domain.quiz.dto.response.QuizResponseDto;
import com.senity.waved.domain.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quiz")
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/{challengeGroupId}")
    public QuizResponseDto getTodaysQuiz(@PathVariable("challengeGroupId") Long challengeGroupId) {
        return quizService.getTodaysQuiz(challengeGroupId);
    }

    @GetMapping("/{challengeGroupId}/dates")
    public QuizResponseDto getQuizByDate(
            @PathVariable("challengeGroupId") Long challengeGroupId,
            @RequestParam("quizDate") Timestamp quizDate
    ) {
        return quizService.getQuizByDate(challengeGroupId, quizDate);
    }
}
