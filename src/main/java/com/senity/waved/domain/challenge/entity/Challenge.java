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

    @Column(name = "latest_group_index")
    private Long latestGroupIndex;

    @Column(name = "image_url")
    private String imageUrl;

    public void updateLatestGroupIndex() {
        latestGroupIndex++;
    }
}
