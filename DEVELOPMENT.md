# Development Guide

## 1. 개발 환경

- AI Assistant: Codex (GPT-5 기반 코딩 에이전트)
- 구현일시 기준: 2026-03-15
- 개발 언어: Java 8
- JDK: Amazon Corretto 1.8.0_342 호환 환경
- 빌드 도구: Gradle Wrapper 7.6.4
- 테스트 프레임워크: JUnit Jupiter 5.10.2
- 커버리지 측정: JaCoCo
- 형상 관리: GitHub

---

## 2. 개발 계획

1. 요구사항 분석
   `docs/REQUIREMENTS.md`를 기준으로 기능 요구사항, 비기능 요구사항, 테스트 시나리오를 정리한다.
2. 설계
   OTA 상태 관리, Lock 판단, 원격 요청 처리, 차량 상태 시뮬레이터를 분리 설계한다.
3. 테스트 작성
   기능 추가 또는 로직 변경 전에 실패하는 테스트를 먼저 작성한다.
4. 구현
   테스트를 통과시키는 최소 구현을 작성하고 중복과 복잡도를 정리한다.
5. 검증
   `./gradlew test jacocoTestCoverageVerification` 또는 Windows에서 `.\gradlew.bat test jacocoTestCoverageVerification`로 검증한다.
6. 형상 관리
   변경 단위를 작게 유지하고, 로직 변경 시 테스트 통과 후 커밋한다.

---

## 3. TDD 개발 원칙

- 모든 개발은 TDD를 기본 원칙으로 한다.
- 기능 추가 또는 로직 변경 시 `Red → Green → Refactor` 순서를 따른다.
- 테스트 코드 없이 기능 코드만 먼저 추가하지 않는다.
- 버그 수정은 재현 테스트를 먼저 작성한 뒤 수정한다.

---

## 4. 로직 변경과 커밋 규칙

- 로직이 변경되는 경우 반드시 관련 테스트를 함께 수정 또는 추가한다.
- 로직 변경 후 `test`와 커버리지 검증이 통과한 상태에서 커밋한다.
- 커밋 메시지는 변경 의도가 드러나도록 작성한다.
- 하나의 커밋에는 하나의 목적을 담는 것을 원칙으로 한다.

예시:

- `feat: block engine control during OTA rebooting`
- `test: add coverage for remote lock release on failed ota`
- `refactor: simplify remote request validation flow`

---

## 5. 문서화 및 주석 규칙

- 각 소스 파일과 함수에는 Doxygen 스타일 주석을 작성한다.
- 주석에는 파일/함수명, 설명, 입력, 출력 정보를 포함한다.
- 추가 Annotation 성격의 메타데이터로 구현일시와 사용 모델/버전을 명시한다.

권장 태그:

- `@file`
- `@brief`
- `@param`
- `@return`
- `@date`
- `@version`
- `@note`

---

## 6. 소스코드 품질 기준

- 함수별 라인 수: 80 이하
- 순환 복잡도: 10 이하
- 주석률: 전체 코드 대비 20% 이상
- 라인 커버리지: 80% 이상

현재 프로젝트는 Gradle JaCoCo 검증으로 라인 커버리지 80% 이상을 확인하도록 구성한다.

---

## 7. 협업 운영 기준

- 요구사항은 `docs/REQUIREMENTS.md`에서 관리한다.
- 개발 환경 및 규칙은 `DEVELOPMENT.md`에서 관리한다.
- 구현 변경은 Pull Request 기반으로 리뷰한다.
- GitHub Code, Issue, Wiki, Action, PR을 협업 채널로 활용한다.
