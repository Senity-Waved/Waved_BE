package com.senity.waved.domain.challenge.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
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
public class Challenge extends BaseEntity {

    @Column(name="title")
    private String title;

    @Column(name="description", columnDefinition = "TEXT")
    private String description;

    @Column(name="challenge_type")
    private ChallengeType challengeType;

    @Column(name="verification_type")
    @Enumerated(EnumType.STRING)
    private VerificationType verificationType;

    @Column(name="verification_description", columnDefinition = "TEXT")
    private String verificationDescription;

    @Column(name="is_free")
    private Boolean isFree;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChallengeGroup> challengeGroups = new ArrayList<>();
}
