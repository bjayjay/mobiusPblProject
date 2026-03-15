package com.mobius.ota.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @file InMemoryEventLogger.java
 * @brief 메모리 기반 이벤트 로거 구현체입니다.
 * @date 2026-03-15
 * @version Codex GPT-5
 */
public final class InMemoryEventLogger implements EventLogger {
    private final List<String> events = new ArrayList<String>();

    /**
     * @brief 이벤트를 메모리에 저장합니다.
     * @param event 기록할 이벤트 메시지
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Override
    public synchronized void log(String event) {
        events.add(event);
    }

    /**
     * @brief 누적된 이벤트 목록을 읽기 전용으로 반환합니다.
     * @param 없음
     * @return 이벤트 목록
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Override
    public synchronized List<String> getEvents() {
        return Collections.unmodifiableList(new ArrayList<String>(events));
    }
}
