package com.senity.waved.domain.event.service;

import com.senity.waved.domain.event.repository.EventRepository;
import com.senity.waved.domain.member.entity.Member;
import com.senity.waved.domain.member.exception.MemberNotFoundException;
import com.senity.waved.domain.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    @Override
    public SseEmitter subscribe(String email) {
        Long memberId = getMemberIdByEmail(email);
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        sendInitEvent(sseEmitter);
        eventRepository.save(memberId, sseEmitter);

        sseEmitter.onCompletion(() -> eventRepository.deleteById(memberId));
        sseEmitter.onTimeout(() -> eventRepository.deleteById(memberId));
        sseEmitter.onError((e) -> eventRepository.deleteById(memberId));

        return sseEmitter;
    }

    @Override
    public String checkNewEvent(String email) {
        Member member = getMemberByEmail(email);
        return new JSONObject().put("newEvent", member.getHasNewEvent()).toString();
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));
    }

    private Long getMemberIdByEmail(String email) {
        Member member = memberRepository.getMemberByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("해당 멤버를 찾을 수 없습니다."));
        return member.getId();
    }

    private void sendInitEvent(SseEmitter sseEmitter) {
        try {
            sseEmitter.send(SseEmitter.event().name("INIT"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
