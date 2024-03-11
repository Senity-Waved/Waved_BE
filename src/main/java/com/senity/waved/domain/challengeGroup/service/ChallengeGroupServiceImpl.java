package com.senity.waved.domain.challengeGroup.service;

import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.exception.ChallengeGroupNotFoundException;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeGroupServiceImpl implements ChallengeGroupService {

    private final ChallengeGroupRepository groupRepository;

    public ChallengeGroupResponseDto getGroupDetail(Long groupId) {
        ChallengeGroup group = getGroupById(groupId);
        return ChallengeGroup.getGroupResponse(group);
    }

    private ChallengeGroup getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new ChallengeGroupNotFoundException("해당 챌린지 그룹을 찾을 수 없습니다."));
    }
}
