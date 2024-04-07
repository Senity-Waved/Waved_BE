package com.senity.waved.domain.event.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class EventRepository {

    // emitters = 모든 연결(Emitter), Long = MemberId
    // ConcurrentHashMap : thread-safe
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(Long id, SseEmitter emitter) {
        emitters.put(id, emitter);
    }

    public void deleteById(Long id) {
        emitters.remove(id);
    }

    public SseEmitter get(Long id) {
        return emitters.get(id);
    }
}
