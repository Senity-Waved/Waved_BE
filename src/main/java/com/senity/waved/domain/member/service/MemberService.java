package com.senity.waved.domain.member.service;

import com.senity.waved.domain.member.dto.GithubInfoDto;
import com.senity.waved.domain.member.dto.ProfileEditDto;
import com.senity.waved.domain.member.dto.response.ProfileInfoResponseDto;
import com.senity.waved.domain.paymentRecord.dto.response.PaymentRecordResponseDto;
import com.senity.waved.domain.review.dto.response.MemberReviewResponseDto;
import org.springframework.data.domain.Page;

public interface MemberService {
    String resolveRefreshToken(String refreshToken);

    void editMemberProfile(String email, ProfileEditDto editDto) ;
    void logout(String email, String token);
    void deleteMember(String email);

    ProfileInfoResponseDto getProfileInfo(String email);
    ProfileEditDto getProfileInfoToEdit(String email);

    GithubInfoDto getGithubInfoToEdit(String email);
    void checkGithubConnection(String email, GithubInfoDto github);
    void saveGithubInfo(String email, GithubInfoDto github);
    void deleteGithubInfo(String email);

    Page<MemberReviewResponseDto> getReviewsPaged(String email, int pageNumber, int pageSize);
    Page<PaymentRecordResponseDto> getMyPaymentRecordsPaged(String email, int pageNumber, int pageSize);
}
