package com.senity.waved.domain.member.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.member.dto.GithubInfoDto;
import com.senity.waved.domain.member.dto.MemberJoinDto;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
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

    @Column(name="github_connection", nullable = true)
    private Boolean githubConnection;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MyChallenge> myChallenges = new ArrayList<>();

    public void updateInfo(MemberJoinDto joinDto) {
        this.nickname = joinDto.getNickname();
        this.birthYear = joinDto.getBirthYear();
        this.gender = joinDto.getGender();
        this.jobTitle = joinDto.getJobTitle();
    }

    public void updateGithubInfo(GithubInfoDto github, Boolean githubConnection) {
        githubId = github.getGithubId();
        githubToken = github.getGithubToken();
        this.githubConnection = githubConnection;
    }
}
