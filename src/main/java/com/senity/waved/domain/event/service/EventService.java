package com.senity.waved.domain.event.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EventService {
    SseEmitter subscribe(String email);

    String checkNewEvent(String email);
}
