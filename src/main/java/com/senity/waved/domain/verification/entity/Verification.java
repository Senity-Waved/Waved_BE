package com.senity.waved.domain.verification.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.challenge.entity.VerificationType;
import com.senity.waved.domain.challengeGroup.dto.response.AdminVerificationListDto;
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
    @Column(nullable = false)
    private Boolean isDeleted; // true -> 삭제

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_group_id")
    private ChallengeGroup challengeGroup;

    @Builder.Default
    @OneToMany(mappedBy = "verification", cascade = CascadeType.ALL)
    private List<Liked> likes = new ArrayList<>();

    public static Verification createGithubVerification(Member member, ChallengeGroup challengeGroup, boolean hasCommitsToday) {
        return Verification.builder()
                .content(String.valueOf(hasCommitsToday))
                .member(member)
                .challengeGroup(challengeGroup)
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

    public static AdminVerificationListDto getAdminVerifications(Verification verification) {
        Member member = verification.getMember();
        return AdminVerificationListDto.builder()
                .nickname(member.getNickname())
                .content(verification.getContent())
                .imageUrl(verification.getImageUrl())
                .link(verification.getLink())
                .verificationId(verification.getId())
                .verificationDate(verification.getCreateDate())
                .isDeleted(verification.getIsDeleted())
                .build();
    }
}
