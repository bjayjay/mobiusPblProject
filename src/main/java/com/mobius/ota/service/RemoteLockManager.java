package com.mobius.ota.service;

import com.mobius.ota.domain.OtaState;
import com.mobius.ota.logging.EventLogger;

/**
 * @file RemoteLockManager.java
 * @brief OTA 상태를 기반으로 원격 제어 잠금 여부를 판단합니다.
 * @date 2026-03-15
 * @version Codex GPT-5
 */
public final class RemoteLockManager {
    private final OtaStateManager otaStateManager;
    private final EventLogger eventLogger;
    private boolean locked;

    /**
     * @brief 원격 잠금 관리자 객체를 생성합니다.
     * @param otaStateManager OTA 상태 관리자
     * @param eventLogger 잠금 상태 이벤트 로거
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public RemoteLockManager(OtaStateManager otaStateManager, EventLogger eventLogger) {
        this.otaStateManager = otaStateManager;
        this.eventLogger = eventLogger;
    }

    /**
     * @brief 현재 OTA 상태 기준 원격 제어 잠금 여부를 반환합니다.
     * @param 없음
     * @return 원격 제어 잠금 여부
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public synchronized boolean isRemoteControlLocked() {
        refreshLockState();
        return locked;
    }

    /**
     * @brief OTA 상태 변화에 맞춰 잠금 상태를 갱신하고 필요 시 로그를 남깁니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    private void refreshLockState() {
        OtaState state = otaStateManager.getCurrentState();
        boolean shouldLock = state.blocksRemoteControl();
        if (shouldLock != locked) {
            locked = shouldLock;
            eventLogger.log(locked
                    ? "Remote control locked by OTA state " + state
                    : "Remote control unlocked by OTA state " + state);
        }
    }
}
