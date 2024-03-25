package com.senity.waved.domain.verification.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.challenge.entity.VerificationType;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.liked.entity.Liked;
import com.senity.waved.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Verification extends BaseEntity {

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "link", columnDefinition = "TEXT")
    private String link;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_type")
    private VerificationType verificationType;

    @Builder.Default
    @Column(name = "likes_count", nullable = false)
    private Long likesCount = 0L;

    @ColumnDefault("FALSE")
    @Column(nullable = true)
    private Boolean isDeleted; // true -> 삭제

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "challenge_group_id")
    private Long challengeGroupId;

    @OneToMany(mappedBy = "verification", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Liked> likes = new ArrayList<>();

    public static Verification createGithubVerification(Member member, ChallengeGroup challengeGroup, boolean hasCommitsToday) {
        return Verification.builder()
                .content(String.valueOf(hasCommitsToday))
                .memberId(member.getId())
                .challengeGroupId(challengeGroup.getId())
                .verificationType(VerificationType.GITHUB)
                .isDeleted(false)
                .build();
    }

    public void addLikeToVerification(Liked like) {
        this.likes.add(like);
        like.setVerification(this);
        this.likesCount++;
    }

    public void removeLikeFromVerification(Liked like) {
        this.likes.remove(like);
        like.setVerification(null);
        this.likesCount--;
    }

    public void markAsDeleted(Boolean b) {
        this.isDeleted = b;
    }
}
