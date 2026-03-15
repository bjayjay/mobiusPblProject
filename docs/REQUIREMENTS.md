# OTA 업데이트 중 원격 기능 안전 제어 시스템

## 프로젝트 개요

본 프로젝트는 **OTA(Over-The-Air) 소프트웨어 업데이트가 진행되는 동안 차량 원격 제어 기능을 안전하게 제한하는 로직**을 구현하는 Java 기반 PBL 프로젝트입니다.

설치와 재부팅처럼 차량 제어와 충돌 가능성이 큰 구간에서는 원격 제어 기능을 차단하고, 조회 기능은 유지함으로써 안전 중심 제어 정책을 학습하는 것을 목표로 합니다.

본 시스템은 **Connected Car Service(CCS)** 환경을 단순화한 구조를 기준으로 설계됩니다.

---

# Software Requirements Specification (SRS)

## 1. 목적

본 문서는 OTA 업데이트 도중 차량 원격 제어 기능을 안전하게 제한하기 위한 소프트웨어 요구사항을 정의합니다.

특히 OTA 설치 및 시스템 재부팅 구간에서 상태 변경 명령이 수행되지 않도록 하고, 업데이트 완료 또는 실패 이후에는 원격 기능을 정상 복구하는 것을 목적으로 합니다.

---

## 2. 범위

본 시스템은 다음 기능을 제공합니다.

- OTA 상태 관리
- OTA 상태에 따른 원격 기능 Lock / Unlock 제어
- 원격 요청 수신 및 처리
- OTA로 인해 거부된 요청에 대한 응답 코드 반환
- 이벤트 로그 기록
- GitHub 기반 소스코드 형상 관리
- CI 환경 확장 가능 구조

---

## 3. 시스템 구성

본 프로젝트는 차량 단말의 **Connected Car Service(CCS)** 소프트웨어 구조를 단순화하여 구현합니다.

Phone App
│
▼
Remote Request Interface
│
▼
Remote Request Manager
│
├── OTA State Manager
├── Remote Lock Manager
└── Vehicle Control Simulator

---

# 4. OTA 상태 정의

시스템은 다음 OTA 상태를 관리합니다.

| 상태 | 설명 |
|-----|------|
| Idle | OTA 진행 중 아님 |
| Downloading | OTA 패키지 다운로드 중 |
| Installing | OTA 설치 진행 중 |
| Rebooting | OTA 적용 후 시스템 재부팅 |
| Completed | OTA 정상 완료 |
| Failed | OTA 실패 |

---

# 5. 원격 기능 정의

## 5.1 Lock 대상 제어 기능

다음 기능은 차량 상태를 변경하는 기능으로 OTA 설치 중 제한됩니다.

- Door Lock / Unlock
- Engine Start / Stop
- Trunk Open / Close
- Climate Control On / Off

## 5.2 허용 기능

다음 기능은 OTA 중에도 허용됩니다.

- 차량 상태 조회
- OTA 진행 상태 조회

---

# 6. Lock 정책

| OTA 상태 | 제어 기능 | 조회 기능 |
|----------|-----------|-----------|
| Idle | 허용 | 허용 |
| Downloading | 허용 | 허용 |
| Installing | 차단 | 허용 |
| Rebooting | 차단 | 허용 |
| Completed | 허용 | 허용 |
| Failed | 허용 | 허용 |

---

# 7. 기능 요구사항

| ID | 요구사항 | 우선순위 |
|----|---------|----------|
| FR-001 | 시스템은 OTA 시작 이벤트를 감지해야 한다 | High |
| FR-002 | 시스템은 OTA 상태를 관리해야 한다 | High |
| FR-003 | 시스템은 현재 OTA 상태를 내부 상태값으로 저장해야 한다 | High |
| FR-004 | OTA 상태가 Installing 또는 Rebooting일 경우 제어 기능을 Lock 해야 한다 | Critical |
| FR-005 | OTA 상태가 Completed 또는 Failed가 되면 Lock 상태를 해제해야 한다 | Critical |
| FR-006 | OTA Downloading 상태에서는 제어 기능을 제한하지 않는다 | Medium |
| FR-007 | 시스템은 폰 앱으로부터 원격 요청을 수신해야 한다 | High |
| FR-008 | Lock 상태에서는 제어 기능 요청을 거부해야 한다 | Critical |
| FR-009 | Lock 상태에서도 조회 기능 요청은 허용해야 한다 | High |
| FR-010 | Unlock 상태에서는 정상적으로 제어 기능을 수행해야 한다 | High |
| FR-011 | OTA로 인해 거부된 요청은 `REMOTE_LOCKED_BY_OTA` 코드로 응답해야 한다 | High |
| FR-012 | 정상 요청은 성공 응답을 반환해야 한다 | Medium |
| FR-013 | 잘못된 요청은 오류 응답을 반환해야 한다 | Medium |
| FR-014 | OTA 시작 이벤트를 로그로 기록해야 한다 | Medium |
| FR-015 | Lock 활성화 이벤트를 로그로 기록해야 한다 | Medium |
| FR-016 | Lock 해제 이벤트를 로그로 기록해야 한다 | Medium |
| FR-017 | OTA로 인해 거부된 요청을 로그로 기록해야 한다 | Medium |
| FR-018 | OTA 상태 변경 시 Lock 정책을 즉시 반영해야 한다 | High |

---

# 8. 비기능 요구사항

| ID | 요구사항 | 우선순위 |
|----|---------|----------|
| NFR-001 | 원격 요청 처리 시간은 500ms 이내여야 한다 | Medium |
| NFR-002 | OTA 상태 변경 후 1초 이내 Lock 정책이 적용되어야 한다 | High |
| NFR-003 | 동일 조건의 요청에 대해 항상 동일한 결과를 반환해야 한다 | High |
| NFR-004 | Installing 및 Rebooting 상태에서는 제어 기능이 실행되면 안 된다 | Critical |
| NFR-005 | 거부된 요청은 식별 가능한 오류 코드를 반환해야 한다 | Medium |
| NFR-006 | OTA 상태 관리와 원격 요청 처리는 분리된 모듈로 구현되어야 한다 | Medium |
| NFR-007 | 모든 기능 요구사항은 테스트 가능해야 한다 | High |
| NFR-008 | OTA 상태 전이 및 요청 처리 기록을 로그로 추적할 수 있어야 한다 | Medium |
| NFR-009 | 소스코드는 GitHub 저장소에서 관리되어야 한다 | High |
| NFR-010 | CI 기반 자동 빌드 및 테스트가 가능해야 한다 | Medium |

---

# 9. 인터페이스 요구사항

| ID | 요구사항 |
|----|---------|
| IR-001 | 시스템은 폰 앱으로부터 원격 제어 요청을 수신해야 한다 |
| IR-002 | 시스템은 OTA 상태 변경 이벤트를 수신해야 한다 |
| IR-003 | 시스템은 요청 처리 결과를 성공 또는 실패로 반환해야 한다 |
| IR-004 | OTA로 인해 거부된 요청은 `REMOTE_LOCKED_BY_OTA` 코드로 반환해야 한다 |

---

# 10. 응답 코드 정의

| 코드 | 설명 |
|-----|------|
| SUCCESS | 요청 정상 처리 |
| REMOTE_LOCKED_BY_OTA | OTA 진행 중으로 인해 원격 제어 차단 |
| INVALID_REQUEST | 잘못된 요청 |
| SYSTEM_ERROR | 시스템 내부 오류 |

예시 응답:

```json
{
  "result": "FAIL",
  "errorCode": "REMOTE_LOCKED_BY_OTA",
  "message": "OTA 설치 중에는 원격 제어를 수행할 수 없습니다."
}
```

---

# 11. 테스트 시나리오

| Test ID | 시나리오 | 기대 결과 |
|--------|---------|-----------|
| TC-001 | OTA 시작 이벤트 발생 | OTA 상태가 Idle → Downloading 또는 Installing으로 변경 |
| TC-002 | OTA 상태가 Installing으로 변경 | 제어 기능 Lock 활성화 |
| TC-003 | OTA 상태가 Rebooting으로 변경 | 제어 기능 Lock 유지 |
| TC-004 | OTA 상태가 Completed로 변경 | Lock 해제 |
| TC-005 | OTA 상태가 Failed로 변경 | Lock 해제 |
| TC-006 | Installing 상태에서 Door Unlock 요청 | 요청 거부 및 `REMOTE_LOCKED_BY_OTA` 반환 |
| TC-007 | Rebooting 상태에서 Engine Start 요청 | 요청 거부 및 `REMOTE_LOCKED_BY_OTA` 반환 |
| TC-008 | Installing 상태에서 차량 상태 조회 요청 | 요청 허용 |
| TC-009 | Rebooting 상태에서 OTA 진행 상태 조회 요청 | 요청 허용 |
| TC-010 | Idle 상태에서 Climate Control 요청 | 정상 수행 |
| TC-011 | Downloading 상태에서 Trunk Open 요청 | 정상 수행 |
| TC-012 | 잘못된 기능 요청 입력 | `INVALID_REQUEST` 반환 |
| TC-013 | OTA 시작, Lock 활성화, Unlock 발생 | 이벤트 로그 기록 확인 |
| TC-014 | 원격 요청 처리 시간 측정 | 응답 시간 500ms 이하 |
| TC-015 | OTA 상태 변경 후 Lock 적용 시간 측정 | 1초 이내 정책 반영 |

---

# 12. 구현 제약사항

| 항목 | 내용 |
|-----|------|
| 구현 언어 | Java |
| 형상 관리 | GitHub |
| 테스트 환경 | 실제 차량 대신 Mock 또는 Simulator 기반 구현 |
| OTA 상태 입력 | 내부 시뮬레이션 또는 테스트 코드로 전달 |
| 원격 요청 인터페이스 | 내부 API 또는 간단한 REST 스타일 구조 |
| CI 환경 | GitHub Actions 적용 가능 |

---

# 13. MVP 구현 범위

## 13.1 1차 구현 범위

- OTA 상태 Enum 정의
- OTA 상태 관리 모듈 구현
- Remote Lock 정책 판단 모듈 구현
- 원격 요청 처리 모듈 구현
- 응답 코드 처리 로직 구현
- 이벤트 로그 출력 기능
- 단위 테스트

## 13.2 향후 확장 기능

- REST API 기반 원격 요청 인터페이스
- GitHub Actions 기반 CI 파이프라인
- OTA 상태 전이 다이어그램 문서화
- 테스트 자동화 및 리포트 생성
- ISO 26262 스타일 요구사항 추적성 관리
