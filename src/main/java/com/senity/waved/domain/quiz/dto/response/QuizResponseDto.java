package com.senity.waved.domain.quiz.dto.response;

import com.senity.waved.domain.quiz.entity.Quiz;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class QuizResponseDto {
    private LocalDate date;
    private String question;

    public QuizResponseDto(Quiz quiz) {
        this.date = quiz.getDate();
        this.question = quiz.getQuestion();
    }
}