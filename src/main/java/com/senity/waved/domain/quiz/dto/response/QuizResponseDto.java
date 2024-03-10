package com.senity.waved.domain.quiz.dto.response;

import com.senity.waved.domain.quiz.entity.Quiz;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class QuizResponseDto {
    private Date date;
    private String question;

    public QuizResponseDto(Quiz quiz) {
        this.date = quiz.getDate();
        this.question = quiz.getQuestion();
    }
}