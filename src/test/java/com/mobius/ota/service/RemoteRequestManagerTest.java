package com.mobius.ota.service;

import com.mobius.ota.domain.OtaState;
import com.mobius.ota.domain.RemoteCommandType;
import com.mobius.ota.domain.RemoteRequest;
import com.mobius.ota.domain.RemoteResponse;
import com.mobius.ota.domain.ResponseCode;
import com.mobius.ota.logging.InMemoryEventLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @file RemoteRequestManagerTest.java
 * @brief OTA 상태별 원격 요청 처리 정책을 검증합니다.
 * @date 2026-03-15
 * @version Codex GPT-5
 */
class RemoteRequestManagerTest {

    private InMemoryEventLogger eventLogger;
    private OtaStateManager otaStateManager;
    private RemoteRequestManager remoteRequestManager;

    /**
     * @brief 테스트마다 독립적인 상태를 갖는 구성 객체를 초기화합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @BeforeEach
    void setUp() {
        eventLogger = new InMemoryEventLogger();
        otaStateManager = new OtaStateManager(eventLogger);
        RemoteLockManager remoteLockManager = new RemoteLockManager(otaStateManager, eventLogger);
        VehicleControlSimulator vehicleControlSimulator = new VehicleControlSimulator();
        remoteRequestManager =
                new RemoteRequestManager(otaStateManager, remoteLockManager, vehicleControlSimulator, eventLogger);
    }

    /**
     * @brief Installing 상태에서 제어 요청이 차단되는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void controlRequestsAreBlockedDuringInstalling() {
        otaStateManager.updateState(OtaState.INSTALLING);

        RemoteResponse response = remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.DOOR_UNLOCK));

        assertFalse(response.isSuccess());
        assertEquals(ResponseCode.REMOTE_LOCKED_BY_OTA, response.getCode());
    }

    /**
     * @brief Rebooting 상태에서 제어 요청이 차단되는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void controlRequestsAreBlockedDuringRebooting() {
        otaStateManager.updateState(OtaState.REBOOTING);

        RemoteResponse response = remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.ENGINE_START));

        assertFalse(response.isSuccess());
        assertEquals(ResponseCode.REMOTE_LOCKED_BY_OTA, response.getCode());
    }

    /**
     * @brief Installing 상태에서도 조회 요청은 허용되는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void queryRequestsRemainAvailableDuringInstalling() {
        otaStateManager.updateState(OtaState.INSTALLING);

        RemoteResponse response = remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.VEHICLE_STATUS));

        assertTrue(response.isSuccess());
        assertTrue(response.getPayload().contains("doorLocked="));
    }

    /**
     * @brief Downloading 상태에서는 제어 요청이 허용되는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void downloadingKeepsControlRequestsAvailable() {
        otaStateManager.updateState(OtaState.DOWNLOADING);

        RemoteResponse response = remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.TRUNK_OPEN));

        assertTrue(response.isSuccess());
        assertEquals(ResponseCode.SUCCESS, response.getCode());
    }

    /**
     * @brief Completed 상태에서 원격 잠금이 해제되는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void completedUnlocksRemoteControlAgain() {
        otaStateManager.updateState(OtaState.INSTALLING);
        remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.DOOR_LOCK));
        otaStateManager.updateState(OtaState.COMPLETED);

        RemoteResponse response = remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.DOOR_LOCK));

        assertTrue(response.isSuccess());
        assertEquals(ResponseCode.SUCCESS, response.getCode());
    }

    /**
     * @brief Failed 상태에서 원격 잠금이 해제되는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void failedUnlocksRemoteControlAgain() {
        otaStateManager.updateState(OtaState.REBOOTING);
        remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.CLIMATE_ON));
        otaStateManager.updateState(OtaState.FAILED);

        RemoteResponse response = remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.CLIMATE_ON));

        assertTrue(response.isSuccess());
        assertEquals(ResponseCode.SUCCESS, response.getCode());
    }

    /**
     * @brief 잘못된 요청이 INVALID_REQUEST로 처리되는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void invalidRequestReturnsInvalidRequestCode() {
        RemoteResponse response = remoteRequestManager.handle(new RemoteRequest(null));

        assertFalse(response.isSuccess());
        assertEquals(ResponseCode.INVALID_REQUEST, response.getCode());
    }

    /**
     * @brief OTA 상태 조회 응답이 현재 상태를 반영하는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void otaStatusQueryReturnsCurrentState() {
        otaStateManager.updateState(OtaState.DOWNLOADING);

        RemoteResponse response = remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.OTA_STATUS));

        assertTrue(response.isSuccess());
        assertEquals("OTA state=DOWNLOADING", response.getPayload());
    }

    /**
     * @brief OTA 상태 변경과 차단 요청이 로그로 남는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void logsStateChangesAndBlockedRequests() {
        otaStateManager.updateState(OtaState.INSTALLING);

        remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.ENGINE_START));

        assertTrue(eventLogger.getEvents().stream().anyMatch(event -> event.contains("OTA state changed")));
        assertTrue(eventLogger.getEvents().stream().anyMatch(event -> event.contains("Blocked remote control request")));
    }

    /**
     * @brief null 요청도 INVALID_REQUEST로 처리되는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void nullRequestReturnsInvalidRequestCode() {
        RemoteResponse response = remoteRequestManager.handle(null);

        assertFalse(response.isSuccess());
        assertEquals(ResponseCode.INVALID_REQUEST, response.getCode());
    }

    /**
     * @brief 제어 명령 수행 후 차량 상태 조회가 반영되는지 검증합니다.
     * @param 없음
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Test
    void vehicleStatusReflectsExecutedControlCommands() {
        remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.DOOR_LOCK));
        remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.ENGINE_START));

        RemoteResponse response = remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.VEHICLE_STATUS));

        assertTrue(response.isSuccess());
        assertTrue(response.getPayload().contains("doorLocked=true"));
        assertTrue(response.getPayload().contains("engineRunning=true"));
    }
}
