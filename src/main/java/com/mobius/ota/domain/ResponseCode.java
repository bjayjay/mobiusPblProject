package com.mobius.ota.domain;

/**
 * @file ResponseCode.java
 * @brief 원격 요청 처리 결과 코드를 정의합니다.
 * @date 2026-03-15
 * @version Codex GPT-5
 */
public enum ResponseCode {
    SUCCESS,
    REMOTE_LOCKED_BY_OTA,
    INVALID_REQUEST,
    SYSTEM_ERROR
}
