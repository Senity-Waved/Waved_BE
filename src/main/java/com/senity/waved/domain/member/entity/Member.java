package com.senity.waved.domain.member.entity;

import com.senity.waved.common.BaseEntity;
import com.senity.waved.domain.member.dto.MemberJoinDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
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
    private Gender gender;

    @Column(name="github_id", nullable=true)
    private String githubId;

    @Column(name="github_token", nullable=true)
    private String githubToken;

    @Column(name="certification_pass", nullable=true)
    private Long certificationPass;

    public void updateInfo(MemberJoinDto joinDto) {
        this.nickname = joinDto.getNickname();
        this.birthYear = joinDto.getBirthYear();
        this.gender = joinDto.getGender();
        this.jobTitle = joinDto.getJobTitle();
    }
}
