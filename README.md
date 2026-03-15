## Project Description
OTA 업데이트 중 차량 상태 변경 원격 기능을 제한하고, 조회 기능은 허용하는 안전 제어 소프트웨어

---

## 프로젝트 목표

- ISO 26262 Part 3, Part 6을 바탕으로 기능 안전 중심의 소프트웨어 개발 방식을 경험한다.
- 요구사항, 설계, 구현, 테스트를 잇는 V-모델 개발 흐름을 이해하고 실습한다.
- GitHub 기반 협업 환경에서 Code, Issue, Wiki, Action, PR 중심의 개발 프로세스를 익힌다.
- 기능 안전 관점에서 소프트웨어 품질을 확보하고 관련 산출물을 체계적으로 관리하는 방법을 학습한다.

---

## 프로젝트 개요

본 프로젝트는 자동차 기능 안전 표준인 ISO 26262와 A-SPICE에서 다루는 개발 절차를 팀 단위로 연습하기 위한 PBL(Project-Based Learning) 형식의 소프트웨어 개발 시뮬레이션이다. 단순 기능 구현에 그치지 않고, 요구사항 정의부터 설계, 구현, 검증까지 이어지는 개발 흐름과 산출물 관리 방식을 함께 다루는 것을 목표로 한다.

---

## 개발 환경

- AI Assistant: Codex (GPT-5 기반 코딩 에이전트)
- Language: Java 8
- Build Tool: Gradle 7.6.4 Wrapper
- Test Framework: JUnit Jupiter 5.10.2

---

## SRS
[요구사항 명세서](./docs/REQUIREMENTS.md)

---

## 개발 규칙

- 모든 구현과 로직 변경은 TDD 기반으로 진행한다.
- 로직 변경 시 관련 테스트를 먼저 추가하거나 갱신한 뒤 구현한다.
- 로직 변경 후 테스트와 품질 검증이 통과한 상태에서 커밋한다.
- 개발 환경, 품질 기준, 협업 방식은 [DEVELOPMENT.md](./DEVELOPMENT.md)에서 관리한다.

---

## 핵심 정책

- Installing, Rebooting → 제어 기능 **Lock**
- Idle, Downloading, Completed, Failed → 제어 기능 **Unlock**
- 조회 기능은 **항상 허용**
- OTA로 인해 요청이 거부될 경우 **`REMOTE_LOCKED_BY_OTA` 응답 코드 반환**

| OTA 상태 | 제어 기능 | 조회 기능 |
|----------|-----------|-----------|
| Idle | 허용 | 허용 |
| Downloading | 허용 | 허용 |
| Installing | 차단 | 허용 |
| Rebooting | 차단 | 허용 |
| Completed | 허용 | 허용 |
| Failed | 허용 | 허용 |
