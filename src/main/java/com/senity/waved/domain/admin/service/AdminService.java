package com.senity.waved.domain.admin.service;

import com.senity.waved.domain.challengeGroup.dto.response.AdminChallengeGroupResponseDto;
import com.senity.waved.domain.verification.dto.response.AdminVerificationDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
    List<AdminChallengeGroupResponseDto> getGroups();
    Page<AdminVerificationDto> getGroupVerificationsPaged(Long challengeGroupId, int pageNumber, int pageSize);
    void deleteVerification(Long groupId, Long verificationId);
}
