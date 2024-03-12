package com.senity.waved.domain.challengeGroup.service;

import com.senity.waved.domain.challengeGroup.dto.response.VerificationListResponseDto;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.exception.ChallengeGroupNotFoundException;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.verification.entity.Verification;
import com.senity.waved.domain.verification.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeGroupServiceImpl implements ChallengeGroupService {

    private final VerificationRepository verificationRepository;
    private final ChallengeGroupRepository challengeGroupRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VerificationListResponseDto> getVerifications(Long challengeGroupId, Timestamp verificationDate) {
        ChallengeGroup challengeGroup = findChallengeGroupById(challengeGroupId);
        LocalDateTime[] dateRange = calculateStartAndEndDate(verificationDate);
        List<Verification> verifications = findVerifications(challengeGroup, dateRange);
        return convertToDtoList(verifications);
    }

    private ChallengeGroup findChallengeGroupById(Long challengeGroupId) {
        return challengeGroupRepository.findById(challengeGroupId)
                .orElseThrow(() -> new ChallengeGroupNotFoundException("챌린지 기수를 찾을 수 없습니다."));
    }

    private LocalDateTime[] calculateStartAndEndDate(Timestamp verificationDate) {
        LocalDateTime startOfDay = verificationDate.toLocalDateTime().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return new LocalDateTime[]{startOfDay, endOfDay};
    }

    private List<Verification> findVerifications(ChallengeGroup challengeGroup, LocalDateTime[] dateRange) {
        return verificationRepository.findByCreateDateBetweenAndChallengeGroup(
                Timestamp.valueOf(dateRange[0]), Timestamp.valueOf(dateRange[1]), challengeGroup);
    }

    private List<VerificationListResponseDto> convertToDtoList(List<Verification> verifications) {
        if (verifications.isEmpty()) {
            throw new IllegalArgumentException("해당 날짜에 존재하는 인증내역이 없습니다.");
        }
        return verifications.stream()
                .map(VerificationListResponseDto::new)
                .collect(Collectors.toList());
    }

}
