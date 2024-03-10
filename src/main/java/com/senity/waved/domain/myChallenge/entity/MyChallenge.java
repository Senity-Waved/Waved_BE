package com.senity.waved.domain.myChallenge.entity;

import com.senity.waved.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MyChallenge extends BaseEntity {

    @ElementCollection
    @Column(name="my_verifs")
    private List<Boolean> myVerifs;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "challenge_group_id")
    private Long challengeGroupId;
}
