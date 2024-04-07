package com.senity.waved.domain.admin.service;

import com.senity.waved.domain.challengeGroup.dto.response.AdminChallengeGroupResponseDto;
import com.senity.waved.domain.challengeGroup.entity.ChallengeGroup;
import com.senity.waved.domain.challengeGroup.exception.ChallengeGroupNotFoundException;
import com.senity.waved.domain.challengeGroup.repository.ChallengeGroupRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.exception.MemberNotFoundException;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.myChallenge.entity.MyChallenge;
import com.senity.waved.domain.myChallenge.exception.MyChallengeNotFoundException;
import com.senity.waved.domain.myChallenge.repository.MyChallengeRepository;
import com.senity.waved.domain.notification.entity.Notification;
import com.senity.waved.domain.notification.repository.NotificationRepository;
import com.senity.waved.domain.verification.dto.response.AdminVerificationDto;
import com.senity.waved.domain.verification.entity.Verification;
import com.senity.waved.domain.verification.exception.VerificationNotFoundException;
import com.senity.waved.domain.verification.repository.VerificationRepository;
import com.senity.waved.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ChallengeGroupRepository groupRepository;
    private final VerificationRepository verificationRepository;
    private final MyChallengeRepository myChallengeRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdminChallengeGroupResponseDto> getGroups() {
        ZonedDateTime todayStart = ZonedDateTime.now().toLocalDate().atStartOfDay(ZoneId.systemDefault());
        List<ChallengeGroup> groups = groupRepository.findChallengeGroupsInProgress(todayStart);

        return groups.stream()
                .map(AdminChallengeGroupResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminVerificationDto> getGroupVerificationsPaged(Long challengeGroupId, int pageNumber, int pageSize) {
        ChallengeGroup challengeGroup = getGroupById(challengeGroupId);
        List<Verification> verifications = verificationRepository.findByChallengeGroupId(challengeGroup.getId());

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<AdminVerificationDto> verificationDtoList = getPaginatedVerificationDtoList(verifications, pageable);

        return new PageImpl<>(verificationDtoList, pageable, verifications.size());
    }

    @Override
    @Transactional
    public void deleteVerification(Long groupId, Long verificationId) {
        Verification verification = getVerificationById(verificationId);
        verification.markAsDeleted(true);

        Member member = getMemberById(verification.getMemberId());
        ChallengeGroup group = getGroupById(verification.getChallengeGroupId());

        MyChallenge myChallenge = getMyChallengeByGroupAndMemberId(group, member.getId());
        myChallenge.deleteVerification(verification.getCreateDate());
        verificationRepository.save(verification);

        createCanceledVerificationNotification(verification, group.getGroupTitle(), member.getId());
        dispatchDeleteEvent(member.getId(), "인증삭제알림");

        member.updateNewEvent(true);
        memberRepository.flush();
    }

    private void dispatchDeleteEvent(Long memberId, String content) {
        String eventFormatted = new JSONObject().put("content", content).toString();
        SseEmitter sseEmitter = eventRepository.get(memberId);
        if (sseEmitter != null) {
            try {
                sseEmitter.send(SseEmitter.event().name("event").data(eventFormatted));
            } catch (IOException e) {
                eventRepository.deleteById(memberId);
            }
        }
    }

    private List<AdminVerificationDto> getPaginatedVerificationDtoList(List<Verification> verifs, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), verifs.size());

        return verifs.subList(start, end)
                .stream()
                .map(verification -> {
                    Member member = getMemberById(verification.getMemberId());
                    return AdminVerificationDto.from(verification, member);
                })
                .collect(Collectors.toList());
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("해당 멤버를 찾을 수 없습니다."));
    }

    private Verification getVerificationById(Long id) {
        return verificationRepository.findById(id)
                .orElseThrow(() -> new VerificationNotFoundException("해당 인증 내역을 찾을 수 없습니다."));
    }

    private ChallengeGroup getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new ChallengeGroupNotFoundException("해당 챌린지 그룹을 찾을 수 없습니다."));
    }

    private MyChallenge getMyChallengeByGroupAndMemberId(ChallengeGroup group, Long memberId) {
        return myChallengeRepository.findByMemberIdAndChallengeGroupIdAndIsPaidTrue(memberId, group.getId())
                .orElseThrow(() -> new MyChallengeNotFoundException("해당 마이챌린지를 찾을 수 없습니다."));
    }

    private void createCanceledVerificationNotification(Verification verification, String groupTitle, Long memberId) {
        int month = verification.getCreateDate().getMonthValue();
        int day = verification.getCreateDate().getDayOfMonth();
        String message = String.format("%s의 %d월 %d일 인증이 취소되었습니다.", groupTitle, month, day);

        Notification newNotification = Notification.of(memberId, "인증 취소 알림", message);
        notificationRepository.save(newNotification);
    }
}

