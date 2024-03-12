package com.senity.waved.domain.quiz.entity;

import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_group_id")
    private ChallengeGroup challengeGroup;
}
