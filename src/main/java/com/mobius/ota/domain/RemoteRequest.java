package com.mobius.ota.domain;

/**
 * @file RemoteRequest.java
 * @brief 원격 요청 데이터를 표현합니다.
 * @date 2026-03-15
 * @version Codex GPT-5
 */
public final class RemoteRequest {
    private final RemoteCommandType commandType;

    /**
     * @brief 원격 요청 객체를 생성합니다.
     * @param commandType 요청할 명령 유형
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public RemoteRequest(RemoteCommandType commandType) {
        this.commandType = commandType;
    }

    /**
     * @brief 요청된 명령 유형을 반환합니다.
     * @param 없음
     * @return 요청 명령 유형
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public RemoteCommandType getCommandType() {
        return commandType;
    }
}
