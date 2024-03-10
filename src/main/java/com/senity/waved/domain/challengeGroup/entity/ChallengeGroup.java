package com.senity.waved.domain.challengeGroup.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.quiz.entity.Quiz;
import com.senity.waved.domain.verification.entity.Verification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ChallengeGroup extends BaseEntity {

    @Column(name="participant_count")
    private Long participantCount;

    @OneToMany(mappedBy = "challengeGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MyChallenge> myChallenges = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @OneToMany(mappedBy = "challengeGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Verification> verification = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Quiz> quizzes = new ArrayList<>();

}
