package com.senity.waved.domain.Challenge.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.ChanllengeGroup.entity.ChallengeGroup;
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
    private String challengeType;

    @Column(name="certification_type")
    private String certificationType;

    @Column(name="certification_description", columnDefinition = "TEXT")
    private String certificationDescription;

    @Column(name="is_free")
    private Boolean isFree;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChallengeGroup> challengeGroups = new ArrayList<>();
}
