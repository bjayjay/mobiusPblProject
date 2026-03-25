package com.mobius.ota.service;

import com.mobius.ota.domain.OtaState;
import com.mobius.ota.logging.EventLogger;

/**
 * @file OtaStateManager.java
 * @brief OTA 상태를 저장하고 상태 변화를 기록합니다.
 * @date 2026-03-15
 * @version Codex GPT-5
 */
public final class OtaStateManager {
    private final EventLogger eventLogger;
    private OtaState currentState = OtaState.IDLE;

    /**
     * @brief OTA 상태 관리자 객체를 생성합니다.
     * @param eventLogger 상태 변경 로그 기록기
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public OtaStateManager(EventLogger eventLogger) {
        this.eventLogger = eventLogger;
    }

    /**
     * @brief 현재 OTA 상태를 반환합니다.
     * @param 없음
     * @return 현재 OTA 상태
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public synchronized OtaState getCurrentState() {
        return currentState;
    }

    /**
     * @brief OTA 상태를 갱신하고 상태 전이 로그를 남깁니다.
     * @param newState 새 OTA 상태
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public synchronized void updateState(OtaState newState) {
        if (newState == null) {
            throw new IllegalArgumentException("OTA state must not be null.");
        }
        OtaState previousState = currentState;
        currentState = newState;
        eventLogger.log("OTA state changed: " + previousState + " -> " + newState);
    }
}
