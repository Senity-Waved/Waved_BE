package com.senity.waved.domain.quiz.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class QuizResponseDto {
    private ZonedDateTime date;
    private String question;

    public QuizResponseDto(ZonedDateTime date, String question) {
        this.date = date;
        this.question = question;
    }
}