package com.senity.waved.domain.admin.service;

import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;
import com.senity.waved.domain.challengeGroup.dto.response.AdminVerificationListDto;
import org.springframework.data.domain.Page;

public interface AdminService {

    Page<ChallengeGroupResponseDto> getGroupsPaged(int pageNumber, int pageSize);

    Page<AdminVerificationListDto> getGroupVerificationsPaged(Long challengeGroupId, int pageNumber, int pageSize);

    void deleteVerification(Long groupId, Long verifId);
}
