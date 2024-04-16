package com.senity.waved.domain.member.service;

import com.senity.waved.base.redis.RedisUtil;
import com.senity.waved.domain.member.dto.GithubInfoDto;
import com.senity.waved.domain.member.dto.ProfileEditDto;
import com.senity.waved.domain.member.dto.response.ProfileInfoResponseDto;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.exception.InvalidRefreshTokenException;
import com.senity.waved.domain.member.exception.MemberNotFoundException;
import com.senity.waved.domain.member.exception.WrongGithubInfoException;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.paymentRecord.dto.response.PaymentRecordResponseDto;
import com.senity.waved.domain.paymentRecord.entity.PaymentRecord;
import com.senity.waved.domain.paymentRecord.repository.PaymentRecordRepository;
import com.senity.waved.domain.review.dto.response.MemberReviewResponseDto;
import com.senity.waved.domain.review.entity.Review;
import com.senity.waved.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRecordRepository paymentRecordRepository;
    private final RedisUtil redisUtil;
    private GitHub github;

    @Transactional(readOnly = true)
    public String resolveRefreshToken(String refreshToken) {
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            throw new InvalidRefreshTokenException("리프레시 토큰이 누락되었거나 올바르지 않습니다.");
        }
        return refreshToken.substring(7);
    }

    @Transactional
    public void editMemberProfile(String email, ProfileEditDto editDto) {
        Member member = getMemberByEmail(email);
        member.updateInfo(editDto);
        memberRepository.save(member);
    }

    @Transactional
    public void logout(String email, String token) {
        redisUtil.setBlackList(token, "accessToken", 10);
        redisUtil.deleteByEmail(email);
    }

    @Transactional
    public void deleteMember(String email) {
        memberRepository.deleteByEmail(email);
        redisUtil.deleteByEmail(email);
    }

    @Transactional
    public ProfileInfoResponseDto getProfileInfo(String email) {
        Member member = getMemberByEmail(email);
        return ProfileInfoResponseDto.from(member);
    }

    @Transactional(readOnly = true)
    public ProfileEditDto getProfileInfoToEdit(String email) {
        Member member = getMemberByEmail(email);
        return ProfileEditDto.from(member);
    }

    @Transactional(readOnly = true)
    public GithubInfoDto getGithubInfoToEdit(String email) {
        Member member = getMemberByEmail(email);
        return GithubInfoDto.from(member);
    }

    @Transactional
    public void checkGithubConnection(String email, GithubInfoDto githubDto) {
        GHUser ghUser = checkCredentials(githubDto);
        if (ghUser == null) {
            throw new WrongGithubInfoException("유효하지 않은 github 정보입니다.");
        }
        saveGithubInfo(email, githubDto);
    }

    @Transactional
    public void saveGithubInfo(String email, GithubInfoDto githubDto) {
        Member member = getMemberByEmail(email);
        member.updateGithubInfo(githubDto);

        memberRepository.save(member);
        memberRepository.flush();
    }

    @Transactional
    public void deleteGithubInfo(String email) {
        Member member = getMemberByEmail(email);
        member.updateGithubInfo(GithubInfoDto.deleteGithubInfo());
    }

    @Transactional(readOnly = true)
    public Page<MemberReviewResponseDto> getReviewsPaged(String email, int pageNumber, int pageSize) {
        Member member = getMemberByEmail(email);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createDate").descending());
        Page<Review> reviewPage = reviewRepository.getReviewByMemberId(member.getId(), pageable);

        List<MemberReviewResponseDto> responseDtoList = getMemberReviewListed(reviewPage);
        return new PageImpl<>(responseDtoList, pageable, reviewPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<PaymentRecordResponseDto> getMyPaymentRecordsPaged(String email, int pageNumber, int pageSize) {
        Member member = getMemberByEmail(email);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createDate").descending());
        // Page<PaymentRecord> paymentRecordPaged = paymentRecordRepository.getPaymentRecordByMemberId(member.getId(), pageable);

        Page<PaymentRecord> paymentRecordPaged = paymentRecordRepository.getPaymentRecordByIdMemberIdWithin(member.getId(), pageable);

        List<PaymentRecordResponseDto> responseDtoList = getPaymentRecordsListed(paymentRecordPaged);
        return new PageImpl<>(responseDtoList, pageable, paymentRecordPaged.getTotalElements());
    }

    private GHUser checkCredentials(GithubInfoDto githubDto) {
        try {
            github = new GitHubBuilder().withOAuthToken(githubDto.getGithubToken()).build();
            github.checkApiUrlValidity();
            return github.getUser(githubDto.getGithubId());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));
    }

    private List<PaymentRecordResponseDto> getPaymentRecordsListed(Page<PaymentRecord> paymentRecordPaged) {
        return paymentRecordPaged.getContent()
                .stream()
                .map(PaymentRecordResponseDto::from)
                .collect(Collectors.toList());
    }

    private List<MemberReviewResponseDto> getMemberReviewListed(Page<Review> reviewPage) {
        return  reviewPage.getContent()
                .stream()
                .map(review -> MemberReviewResponseDto.from(review))
                .collect(Collectors.toList());
    }
}
