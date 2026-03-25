package com.mobius.ota.service;

import com.mobius.ota.domain.OtaState;
import com.mobius.ota.domain.RemoteCommandType;
import com.mobius.ota.domain.RemoteResponse;
import com.mobius.ota.domain.ResponseCode;
import com.mobius.ota.logging.InMemoryEventLogger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @file CoreComponentTest.java
 * @brief 핵심 도메인과 보조 컴포넌트의 동작을 검증합니다.
 * @date 2026-03-15
 * @version Codex GPT-5
 */
class CoreComponentTest {

    /**
     * @brief OTA 상태별 원격 제어 차단 정책을 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void otaStateBlockPolicyMatchesRequirement() {
        assertFalse(OtaState.IDLE.blocksRemoteControl());
        assertFalse(OtaState.DOWNLOADING.blocksRemoteControl());
        assertTrue(OtaState.INSTALLING.blocksRemoteControl());
        assertTrue(OtaState.REBOOTING.blocksRemoteControl());
        assertFalse(OtaState.COMPLETED.blocksRemoteControl());
        assertFalse(OtaState.FAILED.blocksRemoteControl());
    }

    /**
     * @brief RemoteResponse의 성공 및 실패 팩토리를 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void remoteResponseFactoryMethodsPopulateFields() {
        RemoteResponse success = RemoteResponse.success("ok", "payload");
        RemoteResponse failure = RemoteResponse.failure(ResponseCode.SYSTEM_ERROR, "error");

        assertTrue(success.isSuccess());
        assertEquals(ResponseCode.SUCCESS, success.getCode());
        assertEquals("payload", success.getPayload());

        assertFalse(failure.isSuccess());
        assertEquals(ResponseCode.SYSTEM_ERROR, failure.getCode());
        assertEquals("error", failure.getMessage());
    }

    /**
     * @brief RemoteLockManager가 OTA 상태에 따라 잠금 이벤트를 반영하는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void remoteLockManagerTracksLockAndUnlockTransitions() {
        InMemoryEventLogger eventLogger = new InMemoryEventLogger();
        OtaStateManager otaStateManager = new OtaStateManager(eventLogger);
        RemoteLockManager remoteLockManager = new RemoteLockManager(otaStateManager, eventLogger);

        assertFalse(remoteLockManager.isRemoteControlLocked());

        otaStateManager.updateState(OtaState.INSTALLING);
        assertTrue(remoteLockManager.isRemoteControlLocked());

        otaStateManager.updateState(OtaState.COMPLETED);
        assertFalse(remoteLockManager.isRemoteControlLocked());
        assertTrue(eventLogger.getEvents().stream().anyMatch(event -> event.contains("locked by OTA state")));
        assertTrue(eventLogger.getEvents().stream().anyMatch(event -> event.contains("unlocked by OTA state")));
    }

    /**
     * @brief 동일한 잠금 상태를 반복 조회해도 잠금 로그가 중복 기록되지 않는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-25
     * @version Codex GPT-5
     */
    @Test
    void remoteLockManagerDoesNotDuplicateTransitionLogs() {
        InMemoryEventLogger eventLogger = new InMemoryEventLogger();
        OtaStateManager otaStateManager = new OtaStateManager(eventLogger);
        RemoteLockManager remoteLockManager = new RemoteLockManager(otaStateManager, eventLogger);

        otaStateManager.updateState(OtaState.INSTALLING);

        assertTrue(remoteLockManager.isRemoteControlLocked());
        assertTrue(remoteLockManager.isRemoteControlLocked());
        assertTrue(remoteLockManager.isRemoteControlLocked());

        long lockLogCount = eventLogger.getEvents().stream()
                .filter(event -> event.contains("locked by OTA state"))
                .count();

        assertEquals(1L, lockLogCount);
    }

    /**
     * @brief null OTA 상태 갱신 요청은 예외로 거부하고 기존 상태를 유지하는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-25
     * @version Codex GPT-5
     */
    @Test
    void otaStateManagerRejectsNullStateUpdate() {
        InMemoryEventLogger eventLogger = new InMemoryEventLogger();
        OtaStateManager otaStateManager = new OtaStateManager(eventLogger);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> otaStateManager.updateState(null));

        assertEquals("OTA state must not be null.", exception.getMessage());
        assertEquals(OtaState.IDLE, otaStateManager.getCurrentState());
        assertTrue(eventLogger.getEvents().isEmpty());
    }

    /**
     * @brief VehicleControlSimulator의 상태 변경과 조회를 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void vehicleControlSimulatorReturnsUpdatedStatus() {
        VehicleControlSimulator simulator = new VehicleControlSimulator();

        simulator.execute(RemoteCommandType.CLIMATE_ON, OtaState.IDLE);
        simulator.execute(RemoteCommandType.TRUNK_OPEN, OtaState.IDLE);
        String status = simulator.execute(RemoteCommandType.VEHICLE_STATUS, OtaState.IDLE);
        String otaStatus = simulator.execute(RemoteCommandType.OTA_STATUS, OtaState.REBOOTING);

        assertTrue(status.contains("trunkOpen=true"));
        assertTrue(status.contains("climateOn=true"));
        assertEquals("OTA state=REBOOTING", otaStatus);
    }

    /**
     * @brief 제어 명령의 on/off 및 open/close 결과가 차량 상태 조회에 반영되는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-25
     * @version Codex GPT-5
     */
    @Test
    void vehicleControlSimulatorReflectsCommandReversalsInStatus() {
        VehicleControlSimulator simulator = new VehicleControlSimulator();

        simulator.execute(RemoteCommandType.DOOR_LOCK, OtaState.IDLE);
        simulator.execute(RemoteCommandType.ENGINE_START, OtaState.IDLE);
        simulator.execute(RemoteCommandType.TRUNK_OPEN, OtaState.IDLE);
        simulator.execute(RemoteCommandType.CLIMATE_ON, OtaState.IDLE);

        simulator.execute(RemoteCommandType.DOOR_UNLOCK, OtaState.IDLE);
        simulator.execute(RemoteCommandType.ENGINE_STOP, OtaState.IDLE);
        simulator.execute(RemoteCommandType.TRUNK_CLOSE, OtaState.IDLE);
        simulator.execute(RemoteCommandType.CLIMATE_OFF, OtaState.IDLE);

        String status = simulator.execute(RemoteCommandType.VEHICLE_STATUS, OtaState.IDLE);

        assertTrue(status.contains("doorLocked=false"));
        assertTrue(status.contains("engineRunning=false"));
        assertTrue(status.contains("trunkOpen=false"));
        assertTrue(status.contains("climateOn=false"));
    }
}
