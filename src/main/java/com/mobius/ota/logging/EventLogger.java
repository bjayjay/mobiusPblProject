package com.mobius.ota.logging;

import java.util.List;

/**
 * @file EventLogger.java
 * @brief 이벤트 로깅 동작을 정의하는 인터페이스입니다.
 * @date 2026-03-15
 * @version Codex GPT-5
 */
public interface EventLogger {
    /**
     * @brief 이벤트 메시지를 기록합니다.
     * @param event 기록할 이벤트 메시지
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    void log(String event);

    /**
     * @brief 기록된 이벤트 목록을 반환합니다.
     * @param 없음
     * @return 이벤트 문자열 목록
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    List<String> getEvents();
}
