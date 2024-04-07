package com.senity.waved.domain.member.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.member.dto.GithubInfoDto;
import com.senity.waved.domain.member.dto.ProfileEditDto;
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
public class Member extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private AuthLevel authLevel;

    @Column(unique = true)
    private String email;

    @Column(name="nickname", nullable=true)
    private String nickname;

    @Column(name="job_title", nullable=true)
    private String jobTitle;

    @Column(name="birth_year", nullable=true)
    private String birthYear;

    @Column(name="gender", nullable=true)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name="github_id", nullable=true)
    private String githubId;

    @Column(name="github_token", nullable=true)
    private String githubToken;

    @Column(name="has_info")
    private Boolean hasInfo;

    @Column(name="has_new_event", nullable=true)
    private Boolean hasNewEvent;

    public void updateInfo(ProfileEditDto editDto) {
        nickname = editDto.getNickname();
        birthYear = editDto.getBirthYear();
        gender = editDto.getGender();
        jobTitle = editDto.getJobTitle();
        hasInfo = true;
    }

    public void updateGithubInfo(GithubInfoDto github) {
        this.githubId = github.getGithubId();
        this.githubToken = github.getGithubToken();
    }

    public void updateNewEvent(Boolean newEvent) {
        this.hasNewEvent = newEvent;
    }

    public boolean isGithubConnected() {
        return this.githubId != null && this.githubToken != null;
    }

    public static Member deletedMember() {
        return Member.builder()
                .nickname("탈퇴한 서퍼")
                .email("")
                .authLevel(AuthLevel.MEMBER)
                .build();
    }
}
