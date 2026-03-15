package com.mobius.ota.service;

import com.mobius.ota.domain.OtaState;
import com.mobius.ota.domain.RemoteCommandType;

/**
 * @file VehicleControlSimulator.java
 * @brief 차량 제어와 상태 조회를 단순 시뮬레이션합니다.
 * @date 2026-03-15
 * @version Codex GPT-5
 */
public final class VehicleControlSimulator {
    private boolean doorLocked;
    private boolean engineRunning;
    private boolean trunkOpen;
    private boolean climateOn;

    /**
     * @brief 주어진 명령을 차량 상태에 반영하거나 조회 결과를 반환합니다.
     * @param commandType 수행할 원격 명령
     * @param otaState 현재 OTA 상태
     * @return 실행 결과 또는 조회 문자열
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public String execute(RemoteCommandType commandType, OtaState otaState) {
        switch (commandType) {
            case DOOR_LOCK:
                doorLocked = true;
                return "Door locked";
            case DOOR_UNLOCK:
                doorLocked = false;
                return "Door unlocked";
            case ENGINE_START:
                engineRunning = true;
                return "Engine started";
            case ENGINE_STOP:
                engineRunning = false;
                return "Engine stopped";
            case TRUNK_OPEN:
                trunkOpen = true;
                return "Trunk opened";
            case TRUNK_CLOSE:
                trunkOpen = false;
                return "Trunk closed";
            case CLIMATE_ON:
                climateOn = true;
                return "Climate control on";
            case CLIMATE_OFF:
                climateOn = false;
                return "Climate control off";
            case VEHICLE_STATUS:
                return vehicleStatus();
            case OTA_STATUS:
                return "OTA state=" + otaState;
            default:
                throw new IllegalArgumentException("Unsupported command: " + commandType);
        }
    }

    /**
     * @brief 현재 차량 내부 상태를 문자열로 반환합니다.
     * @param 없음
     * @return 차량 상태 문자열
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    private String vehicleStatus() {
        return "doorLocked=" + doorLocked
                + ", engineRunning=" + engineRunning
                + ", trunkOpen=" + trunkOpen
                + ", climateOn=" + climateOn;
    }
}
