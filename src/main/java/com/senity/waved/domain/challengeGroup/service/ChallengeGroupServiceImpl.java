package com.senity.waved.domain.challengeGroup.service;

import com.senity.waved.domain.challenge.entity.Challenge;
import com.senity.waved.domain.challenge.exception.ChallengeNotFoundException;
import com.senity.waved.domain.challenge.repository.ChallengeRepository;
import com.senity.waved.domain.challengeGroup.dto.response.ChallengeGroupResponseDto;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.exception.ChallengeGroupNotFoundException;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.liked.repository.LikedRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.exception.MemberNotFoundException;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.myChallenge.exception.AlreadyMyChallengeExistsException;
import com.senity.waved.domain.myChallenge.repository.MyChallengeRepository;
import com.senity.waved.domain.paymentRecord.entity.PaymentRecord;
import com.senity.waved.domain.paymentRecord.entity.PaymentStatus;
import com.senity.waved.domain.paymentRecord.repository.PaymentRecordRepository;
import com.senity.waved.domain.verification.dto.response.LinkVerificationResponseDto;
import com.senity.waved.domain.verification.dto.response.PictureVerificationResponseDto;
import com.senity.waved.domain.verification.dto.response.TextVerificationResponseDto;
import com.senity.waved.domain.verification.dto.response.VerificationResponseDto;
import com.senity.waved.domain.verification.entity.Verification;
import com.senity.waved.domain.verification.exception.VerifyNotFoundOnDateException;
import com.senity.waved.domain.verification.exception.WrongVerificationTypeException;
import com.senity.waved.domain.verification.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeGroupServiceImpl implements ChallengeGroupService {

    private final MemberRepository memberRepository;
    private final MyChallengeRepository myChallengeRepository;
    private final VerificationRepository verificationRepository;
    private final ChallengeGroupRepository challengeGroupRepository;
    private final PaymentRecordRepository paymentRecordRepository;
    private final ChallengeRepository challengeRepository;
    private final LikedRepository likedRepository;

    // TODO 대기중인 챌린지만 신청 가능하게 설정
    @Override
    @Transactional
    public Long applyForChallengeGroup(String email, Long groupId, Long deposit) {
        Member member = getMemberByEmail(email);
        ChallengeGroup group = getGroupById(groupId);
        checkMyChallengeExistence(member.getId(), groupId);

        MyChallenge newMyChallenge = MyChallenge.of(member, group, deposit);
        savePaymentRecordWhenDepositZero(newMyChallenge, member.getId());

        myChallengeRepository.save(newMyChallenge);
        group.addGroupParticipantCount();
        return newMyChallenge.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public ChallengeGroupResponseDto getGroupDetail(String email, Long groupId) {
        ChallengeGroup group = getGroupById(groupId);
        Challenge challenge = getChallengeById(group.getChallengeId());
        if (Objects.isNull(email)) {
            return ChallengeGroupResponseDto.of(group, challenge, -1L);
        }

        Member member = getMemberByEmail(email);
        Optional<MyChallenge> myChallenge = myChallengeRepository.findByMemberIdAndChallengeGroupIdAndIsPaidTrue(member.getId(), group.getId());

        Long myChallengeId = myChallenge.isPresent() ? myChallenge.get().getId() : -1L;
        return ChallengeGroupResponseDto.of(group, challenge, myChallengeId);
    }

    //TODO: 페이징
    @Override
    @Transactional(readOnly = true)
    public List<VerificationResponseDto> getVerifications(String email, Long challengeGroupId, Timestamp verificationDate) {
        return getVerificationsByUserAndGroup(email, challengeGroupId, verificationDate, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VerificationResponseDto> getUserVerifications(String email, Long challengeGroupId, Timestamp verificationDate) {
        return getVerificationsByUserAndGroup(email, challengeGroupId, verificationDate, true);
    }

    private List<VerificationResponseDto> getVerificationsByUserAndGroup(String email, Long challengeGroupId, Timestamp verificationDate, boolean isUserVerifications) {
        Member member = getMemberByEmail(email);
        ChallengeGroup challengeGroup = getGroupById(challengeGroupId);
        ZonedDateTime[] dateRange = calculateStartAndEndDate(verificationDate);
        List<Verification> verifications;

        if (isUserVerifications) {
            verifications = findVerificationsByMemberAndGroupAndDateRange(member, challengeGroup, dateRange);
        } else {
            verifications = findVerifications(challengeGroup, dateRange);
        }
        return convertToDtoList(verifications, member);
    }

    private Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException("해당 챌린지를 찾을 수 없습니다."));
    }

    private Member getMemberById(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);

        if (optionalMember.isEmpty()) {
            return Member.deletedMember();
        } return optionalMember.get();
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.getMemberByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원을 찾을 수 없습니다."));
    }

    private ChallengeGroup getGroupById(Long id) {
        return challengeGroupRepository.findById(id)
                .orElseThrow(() -> new ChallengeGroupNotFoundException("해당 챌린지 그룹을 찾을 수 없습니다."));
    }

    private ZonedDateTime[] calculateStartAndEndDate(Timestamp verificationDate) {
        ZonedDateTime startOfDay = verificationDate.toLocalDateTime().atZone(ZoneId.of("Asia/Seoul")).toLocalDate().atStartOfDay(ZoneId.of("Asia/Seoul"));
        ZonedDateTime endOfDay = startOfDay.withHour(23).withMinute(59).withSecond(59).withNano(999000000); // 23:59:59.999

        return new ZonedDateTime[]{startOfDay, endOfDay};
    }

    private List<Verification> findVerificationsByMemberAndGroupAndDateRange(Member member, ChallengeGroup challengeGroup, ZonedDateTime[] dateRange) {
        return verificationRepository.findByMemberIdAndChallengeGroupIdAndCreateDateBetweenAndIsDeletedFalse(
                member.getId(),
                challengeGroup.getId(),
                dateRange[0],
                dateRange[1]
        );
    }

    private List<Verification> findVerifications(ChallengeGroup challengeGroup, ZonedDateTime[] dateRange) {
        return verificationRepository.findByCreateDateBetweenAndChallengeGroupAndIsDeletedFalse (
                ZonedDateTime.of(dateRange[0].toLocalDate(), dateRange[0].toLocalTime(), dateRange[0].getZone()),
                ZonedDateTime.of(dateRange[1].toLocalDate(), dateRange[1].toLocalTime(), dateRange[1].getZone()),
                challengeGroup.getId()
        );
    }

    private List<VerificationResponseDto> convertToDtoList(List<Verification> verifications, Member member) {
        if (verifications.isEmpty()) {
            throw new VerifyNotFoundOnDateException("해당 날짜에 존재하는 인증내역이 없습니다.");
        }
        return verifications.stream()
                .map(verification -> {
                    Member verificationMember = getMemberById(verification.getMemberId());
                    switch (verification.getVerificationType()) {
                        case TEXT:
                            return TextVerificationResponseDto.of(verification, verificationMember, isLikedByMember(verification, member));
                        case PICTURE:
                            return PictureVerificationResponseDto.of(verification, verificationMember, isLikedByMember(verification, member));
                        case LINK:
                            return LinkVerificationResponseDto.of(verification, verificationMember, isLikedByMember(verification, member));
                        case GITHUB:
                            break;
                        default:
                            throw new WrongVerificationTypeException("유효하지 않은 인증타입입니다.");
                    } return null;
                })
                .collect(Collectors.toList());
    }

    private boolean isLikedByMember(Verification verification, Member member) {
        return likedRepository.existsByMemberIdAndVerification(member.getId(), verification);
    }

    private void savePaymentRecordWhenDepositZero(MyChallenge myChallenge, Long memberId) {
        if (myChallenge.getDeposit() == 0) {
            ChallengeGroup group = getGroupById(myChallenge.getChallengeGroupId());
            String groupTitle = group.getGroupTitle();

            PaymentRecord paymentRecord = PaymentRecord.of(PaymentStatus.APPLIED, memberId, myChallenge, groupTitle);
            paymentRecordRepository.save(paymentRecord);
        }
    }

    private void checkMyChallengeExistence(Long memberId, Long groupId) {
        Optional<MyChallenge> myChallenge = myChallengeRepository.findByMemberIdAndChallengeGroupIdAndIsPaidTrue(memberId, groupId);
        if (myChallenge.isPresent()) {
            throw new AlreadyMyChallengeExistsException("이미 신청되어있는 챌린지 그룹 입니다.");
        }
    }
}