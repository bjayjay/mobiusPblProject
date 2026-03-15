package com.mobius.ota.service;

import com.mobius.ota.domain.OtaState;
import com.mobius.ota.domain.RemoteCommandType;
import com.mobius.ota.domain.RemoteRequest;
import com.mobius.ota.domain.RemoteResponse;
import com.mobius.ota.domain.ResponseCode;
import com.mobius.ota.logging.EventLogger;

/**
 * @file RemoteRequestManager.java
 * @brief 원격 요청의 유효성 검사, 잠금 정책 적용, 실제 처리까지 담당합니다.
 * @date 2026-03-15
 * @version Codex GPT-5
 */
public final class RemoteRequestManager {
    private final OtaStateManager otaStateManager;
    private final RemoteLockManager remoteLockManager;
    private final VehicleControlSimulator vehicleControlSimulator;
    private final EventLogger eventLogger;

    /**
     * @brief 원격 요청 처리 매니저를 생성합니다.
     * @param otaStateManager OTA 상태 관리자
     * @param remoteLockManager 원격 잠금 관리자
     * @param vehicleControlSimulator 차량 동작 시뮬레이터
     * @param eventLogger 이벤트 로거
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public RemoteRequestManager(
            OtaStateManager otaStateManager,
            RemoteLockManager remoteLockManager,
            VehicleControlSimulator vehicleControlSimulator,
            EventLogger eventLogger) {
        this.otaStateManager = otaStateManager;
        this.remoteLockManager = remoteLockManager;
        this.vehicleControlSimulator = vehicleControlSimulator;
        this.eventLogger = eventLogger;
    }

    /**
     * @brief 원격 요청을 처리하고 결과 응답을 반환합니다.
     * @param request 처리할 원격 요청
     * @return 처리 결과 응답
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public RemoteResponse handle(RemoteRequest request) {
        if (request == null || request.getCommandType() == null) {
            eventLogger.log("Rejected invalid remote request");
            return RemoteResponse.failure(ResponseCode.INVALID_REQUEST, "Request or command type is missing.");
        }

        RemoteCommandType commandType = request.getCommandType();
        OtaState otaState = otaStateManager.getCurrentState();

        if (commandType.isControlCommand() && remoteLockManager.isRemoteControlLocked()) {
            eventLogger.log("Blocked remote control request " + commandType + " due to OTA state " + otaState);
            return RemoteResponse.failure(
                    ResponseCode.REMOTE_LOCKED_BY_OTA,
                    "Remote control is locked while OTA is " + otaState + '.');
        }

        try {
            String payload = vehicleControlSimulator.execute(commandType, otaState);
            eventLogger.log("Handled remote request " + commandType + " in OTA state " + otaState);
            return RemoteResponse.success("Request completed successfully.", payload);
        } catch (IllegalArgumentException ex) {
            eventLogger.log("Rejected unsupported remote request " + commandType);
            return RemoteResponse.failure(ResponseCode.INVALID_REQUEST, ex.getMessage());
        } catch (RuntimeException ex) {
            eventLogger.log("System error while handling " + commandType + ": " + ex.getMessage());
            return RemoteResponse.failure(ResponseCode.SYSTEM_ERROR, "Unexpected system error.");
        }
    }
}
