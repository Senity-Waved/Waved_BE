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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ChallengeGroup extends BaseEntity {

    @Column(name = "group_index")
    private Long groupIndex;

    @Column(name = "title")
    private String groupTitle;

    @Column(name = "participant_count")
    private Long participantCount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @OneToMany(mappedBy = "challengeGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MyChallenge> myChallenges = new ArrayList<>();

    @OneToMany(mappedBy = "challengeGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Verification> verifications  = new ArrayList<>();

    @OneToMany(mappedBy = "challengeGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Quiz> quizzes = new ArrayList<>();
}
