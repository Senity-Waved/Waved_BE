package com.senity.waved.domain.admin.service;

import com.senity.waved.domain.challengeGroup.dto.response.AdminChallengeGroupResponseDto;
import com.senity.waved.domain.challengeGroup.dto.response.AdminVerificationListDto;
import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.exception.ChallengeGroupNotFoundException;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.verification.entity.Verification;
import com.senity.waved.domain.verification.exception.VerificationNotFoundException;
import com.senity.waved.domain.verification.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ChallengeGroupRepository groupRepository;
    private final VerificationRepository verificationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdminChallengeGroupResponseDto> getGroups() {
        ZonedDateTime todayStart = ZonedDateTime.now().toLocalDate().atStartOfDay(ZoneId.systemDefault());

        List<ChallengeGroup> groups = groupRepository.findChallengeGroupsInProgress(todayStart);
        return groups.stream()
                .map(AdminChallengeGroupResponseDto::fromChallengeGroup)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminVerificationListDto> getGroupVerificationsPaged(Long challengeGroupId, int pageNumber, int pageSize) {
        ChallengeGroup challengeGroup = getGroupById(challengeGroupId);
        List<Verification> verifications = findVerificationsByGroup(challengeGroup);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<AdminVerificationListDto> verificationListDtoList = getPaginatedVerificationDtoList(verifications, pageable);

        return new PageImpl<>(verificationListDtoList, pageable, verifications.size());
    }

    @Override
    @Transactional
    public void deleteVerification(Long groupId, Long verifId) {
        Verification verification = verificationRepository.findById(verifId)
                .orElseThrow(() -> new VerificationNotFoundException("해당 인증 내역을 찾을 수 없습니다."));
        verification.markAsDeleted(true);
        verificationRepository.save(verification);
    }

    private List<AdminVerificationListDto> getPaginatedVerificationDtoList(List<Verification> verifs, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), verifs.size());

        return verifs.subList(start, end)
                .stream()
                .map(Verification::getAdminVerifications)
                .collect(Collectors.toList());
    }

    private List<ChallengeGroupResponseDto> getPaginatedGroupResponseDtoList(List<ChallengeGroup> groups, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), groups.size());

        return groups.subList(start, end)
                .stream()
                .map(ChallengeGroup::getGroupAdminResponse)
                .collect(Collectors.toList());
    }

    private List<Verification> findVerificationsByGroup(ChallengeGroup challengeGroup) {
        return verificationRepository.findByChallengeGroup(challengeGroup);
    }

    private ChallengeGroup getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new ChallengeGroupNotFoundException("해당 챌린지 그룹을 찾을 수 없습니다."));
    }
}
