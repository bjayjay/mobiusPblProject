package com.mobius.ota.domain;

/**
 * @file RemoteCommandType.java
 * @brief 원격 요청에서 처리할 수 있는 명령 종류를 정의합니다.
 * @date 2026-03-15
 * @version Codex GPT-5
 */
public enum RemoteCommandType {
    DOOR_LOCK(RequestCategory.CONTROL),
    DOOR_UNLOCK(RequestCategory.CONTROL),
    ENGINE_START(RequestCategory.CONTROL),
    ENGINE_STOP(RequestCategory.CONTROL),
    TRUNK_OPEN(RequestCategory.CONTROL),
    TRUNK_CLOSE(RequestCategory.CONTROL),
    CLIMATE_ON(RequestCategory.CONTROL),
    CLIMATE_OFF(RequestCategory.CONTROL),
    VEHICLE_STATUS(RequestCategory.QUERY),
    OTA_STATUS(RequestCategory.QUERY);

    private final RequestCategory category;

    /**
     * @brief 명령 유형에 카테고리를 연결합니다.
     * @param category 제어/조회 구분
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    RemoteCommandType(RequestCategory category) {
        this.category = category;
    }

    /**
     * @brief 현재 명령이 차량 상태를 변경하는 제어 명령인지 확인합니다.
     * @param 없음
     * @return 제어 명령 여부
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public boolean isControlCommand() {
        return category == RequestCategory.CONTROL;
    }
}
