package com.senity.waved.domain.member.entity;

import com.senity.waved.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MemberEntity extends BaseEntity {

    @Column(unique = true)
    private String email;

    @Column(name="nickname")
    private String nickname;

    @Column(name="job_title")
    private String jobTitle;

    @Column(name="birth_date")
    private LocalDate birthDate;

    @Column(name="gender")
    private boolean gender;

    @Column(name="github_id")
    private String githubId;

    @Column(name="github_token")
    private String githubToken;

    @Column(name="reward")
    private Long reward;
}
