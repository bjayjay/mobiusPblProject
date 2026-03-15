package com.mobius.ota.domain;

/**
 * @file OtaState.java
 * @brief OTA 진행 상태를 정의합니다.
 * @date 2026-03-15
 * @version Codex GPT-5
 */
public enum OtaState {
    IDLE,
    DOWNLOADING,
    INSTALLING,
    REBOOTING,
    COMPLETED,
    FAILED;

    /**
     * @brief 현재 OTA 상태가 원격 제어를 차단해야 하는지 판단합니다.
     * @param 없음
     * @return 원격 제어 차단 여부
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public boolean blocksRemoteControl() {
        return this == INSTALLING || this == REBOOTING;
    }
}
