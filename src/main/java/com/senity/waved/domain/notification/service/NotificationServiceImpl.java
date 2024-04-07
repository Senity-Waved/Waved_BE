package com.senity.waved.domain.notification.service;

import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.exception.MemberNotFoundException;
import com.senity.waved.domain.member.repository.MemberRepository;
import com.senity.waved.domain.notification.dto.response.NotificationResponseDto;
import com.senity.waved.domain.notification.entity.Notification;
import com.senity.waved.domain.notification.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    //TODO: 오래된 알림 삭제 처리
    @Override
    public List<NotificationResponseDto> getNotifications(String email) {
        Member member = getMemberByEmail(email);

        List<Notification> notifications = notificationRepository.findByMemberId(member.getId());
        member.updateNewEvent(false);
        memberRepository.flush();

        return notifications.stream()
                .map(NotificationResponseDto::of)
                .collect(Collectors.toList());
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));
    }
}
