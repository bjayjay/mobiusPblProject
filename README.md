## Project Description
OTA 업데이트 중 차량 상태 변경 원격 기능을 제한하고, 조회 기능은 허용하는 안전 제어 소프트웨어

---

## SRS
[docs/SRS.md] (https://github.com/bjayjay/mobiusPblProject/blob/main/docs/SRS.md)

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
