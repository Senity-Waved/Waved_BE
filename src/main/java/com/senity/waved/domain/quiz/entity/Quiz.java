package com.senity.waved.domain.quiz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private String question;

    @Column(name = "challenge_group_id")
    private Long challengeGroupId;

}
