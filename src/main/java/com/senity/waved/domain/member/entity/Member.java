package com.senity.waved.domain.member.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.member.dto.GithubInfoDto;
import com.senity.waved.domain.member.dto.ProfileEditDto;
import com.senity.waved.domain.member.dto.response.ProfileInfoResponseDto;
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

    public static ProfileInfoResponseDto getProfileInfoStatic(Member member) {
        return ProfileInfoResponseDto.builder()
                .nickname(member.getNickname())
                .jobTitle(member.getJobTitle())
                .githubId(member.getGithubId())
                .build();
    }

    public static ProfileEditDto getProfileEditStatic(Member member) {
        return ProfileEditDto.builder()
                .nickname(member.getNickname())
                .jobTitle(member.getJobTitle())
                .birthYear(member.getBirthYear())
                .gender(member.getGender())
                .build();
    }

    public static GithubInfoDto getGithubInfoStatic(Member member) {
        return GithubInfoDto.builder()
                .githubId(member.getGithubId())
                .githubToken(member.getGithubToken())
                .build();
    }

    public boolean isGithubConnected() {
        return this.githubId != null && !this.githubId.isEmpty() && this.githubToken != null && !this.githubToken.isEmpty();
    }
}
