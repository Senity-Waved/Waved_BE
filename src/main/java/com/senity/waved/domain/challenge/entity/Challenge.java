package com.senity.waved.domain.challenge.entity;

import com.senity.waved.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Challenge extends BaseEntity {

    @Column(name="title")
    private String title;

    @Column(name="description", columnDefinition = "TEXT")
    private String description;

    @Column(name="verification_type")
    @Enumerated(EnumType.STRING)
    private VerificationType verificationType;

    @Column(name="verification_description", columnDefinition = "TEXT")
    private String verificationDescription;

    @Column(name="is_free")
    private Boolean isFree;

/*
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
*/
/*
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ChallengeGroup> groups = new ArrayList<>();*/
}
