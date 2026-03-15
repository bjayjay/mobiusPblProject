package com.mobius.ota;

import com.mobius.ota.domain.OtaState;
import com.mobius.ota.domain.RemoteCommandType;
import com.mobius.ota.domain.RemoteRequest;
import com.mobius.ota.domain.RemoteResponse;
import com.mobius.ota.logging.EventLogger;
import com.mobius.ota.logging.InMemoryEventLogger;
import com.mobius.ota.service.OtaStateManager;
import com.mobius.ota.service.RemoteLockManager;
import com.mobius.ota.service.RemoteRequestManager;
import com.mobius.ota.service.VehicleControlSimulator;

/**
 * @file Application.java
 * @brief 프로젝트의 실행 예시를 제공하는 진입점입니다.
 * @date 2026-03-15
 * @version Codex GPT-5
 * @note OTA 상태 변화와 원격 요청 처리 흐름을 간단히 시연합니다.
 */
public final class Application {

    /**
     * @brief 유틸리티 클래스의 인스턴스 생성을 방지합니다.
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    private Application() {
    }

    /**
     * @brief 샘플 OTA 상태 전이와 원격 요청 처리 결과를 출력합니다.
     * @param args 실행 인자
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public static void main(String[] args) {
        EventLogger eventLogger = new InMemoryEventLogger();
        OtaStateManager otaStateManager = new OtaStateManager(eventLogger);
        VehicleControlSimulator vehicleControlSimulator = new VehicleControlSimulator();
        RemoteLockManager remoteLockManager = new RemoteLockManager(otaStateManager, eventLogger);
        RemoteRequestManager remoteRequestManager =
                new RemoteRequestManager(otaStateManager, remoteLockManager, vehicleControlSimulator, eventLogger);

        otaStateManager.updateState(OtaState.DOWNLOADING);
        RemoteResponse allowed = remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.TRUNK_OPEN));
        otaStateManager.updateState(OtaState.INSTALLING);
        RemoteResponse blocked = remoteRequestManager.handle(new RemoteRequest(RemoteCommandType.ENGINE_START));

        System.out.println("Downloading response: " + allowed);
        System.out.println("Installing response: " + blocked);
        System.out.println("Event log:");
        for (String event : eventLogger.getEvents()) {
            System.out.println(" - " + event);
        }
    }
}
