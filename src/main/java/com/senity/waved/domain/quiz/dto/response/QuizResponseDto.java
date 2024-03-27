package com.senity.waved.domain.quiz.dto.response;

import com.senity.waved.domain.quiz.entity.Quiz;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class QuizResponseDto {
    private ZonedDateTime date;
    private String question;

    public QuizResponseDto(Quiz quiz) {
        this.date = quiz.getDate().plusHours(9);
        this.question = quiz.getQuestion();
    }
}