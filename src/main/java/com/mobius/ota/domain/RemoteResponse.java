package com.mobius.ota.domain;

/**
 * @file RemoteResponse.java
 * @brief 원격 요청 처리 결과를 표현합니다.
 * @date 2026-03-15
 * @version Codex GPT-5
 */
public final class RemoteResponse {
    private final boolean success;
    private final ResponseCode code;
    private final String message;
    private final String payload;

    /**
     * @brief 응답 객체를 초기화합니다.
     * @param success 성공 여부
     * @param code 응답 코드
     * @param message 설명 메시지
     * @param payload 부가 데이터
     * @return 반환값 없음
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    private RemoteResponse(boolean success, ResponseCode code, String message, String payload) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.payload = payload;
    }

    /**
     * @brief 성공 응답을 생성합니다.
     * @param message 성공 메시지
     * @param payload 응답 데이터
     * @return 성공 응답 객체
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public static RemoteResponse success(String message, String payload) {
        return new RemoteResponse(true, ResponseCode.SUCCESS, message, payload);
    }

    /**
     * @brief 실패 응답을 생성합니다.
     * @param code 실패 코드
     * @param message 실패 메시지
     * @return 실패 응답 객체
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public static RemoteResponse failure(ResponseCode code, String message) {
        return new RemoteResponse(false, code, message, null);
    }

    /**
     * @brief 응답 성공 여부를 반환합니다.
     * @param 없음
     * @return 성공 여부
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @brief 응답 코드를 반환합니다.
     * @param 없음
     * @return 응답 코드
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public ResponseCode getCode() {
        return code;
    }

    /**
     * @brief 응답 메시지를 반환합니다.
     * @param 없음
     * @return 메시지 문자열
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public String getMessage() {
        return message;
    }

    /**
     * @brief 응답 부가 데이터를 반환합니다.
     * @param 없음
     * @return 부가 데이터 문자열
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    public String getPayload() {
        return payload;
    }

    /**
     * @brief 응답 객체를 문자열로 변환합니다.
     * @param 없음
     * @return 문자열 표현
     * @date 2026-03-15
     * @version Codex GPT-5
     */
    @Override
    public String toString() {
        return "RemoteResponse{"
                + "success=" + success
                + ", code=" + code
                + ", message='" + message + '\''
                + ", payload='" + payload + '\''
                + '}';
    }
}
