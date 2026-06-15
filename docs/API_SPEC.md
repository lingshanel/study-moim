# 스터디모임 API 명세

이 문서는 현재 구현된 스터디모임 REST API를 정리한 문서입니다.

- 인증 방식: 세션 기반 로그인
- 응답 형식: JSON
- 기본 서버: `http://localhost:8080`
- 관리자 페이지: `/ops/study-moim-admin`

## 공통 에러 응답

```json
{
  "message": "요청 값을 확인해주세요."
}
```

| Status | 의미 |
| --- | --- |
| 400 | 요청 값 오류 |
| 401 | 로그인 필요 |
| 403 | 권한 없음 |
| 404 | 리소스 없음 |
| 500 | 서버 오류 |

## Auth

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/api/auth/signup` | 회원가입 | - |
| POST | `/api/auth/login` | 일반 로그인 | - |
| POST | `/api/admin/login` | 관리자 로그인 | - |
| POST | `/api/auth/logout` | 로그아웃 | 필요 |
| GET | `/api/users/me` | 현재 로그인 사용자 조회 | 필요 |
| POST | `/api/auth/find-nickname` | 이메일로 닉네임 찾기 | - |
| POST | `/api/auth/password-reset/code` | 비밀번호 재설정 인증번호 전송 | - |
| POST | `/api/auth/password-reset/verify` | 인증번호 확인 및 일회용 재설정 토큰 발급 | - |
| POST | `/api/auth/password-reset/confirm` | 토큰으로 새 비밀번호 설정 | - |

## Users

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| PATCH | `/api/users/me/password` | 내 비밀번호 변경 | 필요 |
| DELETE | `/api/users/me` | 회원 탈퇴 | 필요 |
| GET | `/api/users/me/bookmarks` | 내 북마크 모집글 조회 | 필요 |

## Categories

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| GET | `/api/categories` | 카테고리 목록 조회 | - |

## Study Posts

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| GET | `/api/study-posts` | 모집글 목록 조회 | - |
| POST | `/api/study-posts` | 모집글 작성 | 필요 |
| GET | `/api/study-posts/{postId}` | 모집글 상세 조회 | - |
| PATCH | `/api/study-posts/{postId}` | 모집글 수정 | 작성자 |
| DELETE | `/api/study-posts/{postId}` | 모집글 삭제 | 작성자 |
| POST | `/api/study-posts/{postId}/image` | 대표 이미지 업로드 | 작성자 |
| PATCH | `/api/study-posts/{postId}/status` | 모집 상태 변경 | 작성자 |

### 목록 검색 파라미터

| Name | 설명 | 예시 |
| --- | --- | --- |
| `category` | 카테고리 slug | `backend` |
| `keyword` | 제목/내용 검색 | `Spring` |
| `authorId` | 작성자 ID | `1` |
| `studyType` | 진행 방식 | `ONLINE`, `OFFLINE`, `HYBRID` |
| `status` | 모집 상태 | `RECRUITING`, `CLOSED` |
| `page` | 페이지 번호 | `0` |
| `size` | 페이지 크기 | `5` |
| `sort` | 정렬 | `latest`, `popular`, `bookmarked` |

## Comments

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/api/study-posts/{postId}/comments` | 댓글/답글 작성 | 필요 |
| GET | `/api/study-posts/{postId}/comments` | 특정 모집글 댓글 목록 조회 | - |
| PATCH | `/api/comments/{commentId}` | 댓글 수정 | 작성자 |
| DELETE | `/api/comments/{commentId}` | 댓글 삭제 | 작성자 |

## Reactions

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/api/study-posts/{postId}/likes` | 좋아요 추가 | 필요 |
| DELETE | `/api/study-posts/{postId}/likes` | 좋아요 취소 | 필요 |
| POST | `/api/study-posts/{postId}/bookmarks` | 북마크 추가 | 필요 |
| DELETE | `/api/study-posts/{postId}/bookmarks` | 북마크 취소 | 필요 |

## Reports

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/api/study-posts/{postId}/reports` | 모집글 신고 | 필요 |
| POST | `/api/comments/{commentId}/reports` | 댓글 신고 | 필요 |
| GET | `/api/admin/reports` | 신고 목록 조회 | ADMIN |
| PATCH | `/api/admin/reports/{reportId}/resolve` | 신고 처리완료 | ADMIN |

### 모집글 신고 요청

```json
{
  "reason": "스터디와 관련 없는 홍보성 내용입니다."
}
```

### 댓글 신고 요청

```json
{
  "reason": "댓글 내용이 부적절합니다."
}
```

신고 규칙:

- 로그인한 사용자만 신고할 수 있습니다.
- 본인이 작성한 모집글이나 댓글은 신고할 수 없습니다.
- 삭제된 댓글은 신고할 수 없습니다.
- 같은 사용자가 같은 대상에 미처리 신고를 중복 등록할 수 없습니다.
- 관리자 신고 목록에서는 `POST`, `COMMENT` 대상으로 구분됩니다.

### 관리자 신고 검색 파라미터

| Name | 설명 | 예시 |
| --- | --- | --- |
| `keyword` | 글 제목/댓글 내용/신고자/사유 검색 | `홍보` |
| `status` | 신고 상태 | `PENDING`, `RESOLVED` |

## Admin

관리자 기능은 일반 사용자 메뉴에 노출하지 않습니다. 개발/운영 단계에서 준비한 슈퍼 관리자 계정으로 숨겨진 운영 페이지에 직접 접근합니다.

```text
/ops/study-moim-admin
```

최초 관리자 계정 생성 환경변수:

```text
ADMIN_ENABLED=true
ADMIN_EMAIL=admin@example.com
ADMIN_NICKNAME=superadmin
ADMIN_PASSWORD=change-this-password
```

기본 설정에서는 관리자 계정을 자동 생성하지 않습니다. 최초 계정 생성 후에는 `ADMIN_ENABLED=false`로 되돌립니다.

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| GET | `/api/admin/users` | 회원 목록/검색 | ADMIN |
| PATCH | `/api/admin/users/{userId}/status` | 회원 상태 변경 | ADMIN |
| DELETE | `/api/admin/study-posts/{postId}` | 모집글 관리자 삭제 | ADMIN |
| DELETE | `/api/admin/comments/{commentId}` | 댓글 관리자 삭제 | ADMIN |
| GET | `/api/admin/reports` | 신고 목록/검색 | ADMIN |
| PATCH | `/api/admin/reports/{reportId}/resolve` | 신고 처리 완료 | ADMIN |
| GET | `/api/admin/action-logs` | 최근 관리자 운영 기록 조회 | ADMIN |

### 관리자 회원 검색 파라미터

| Name | 설명 | 예시 |
| --- | --- | --- |
| `keyword` | 닉네임/이메일 검색 | `test1` |

## 현재 진행 상태

### 1차 완료

- 회원가입, 로그인, 로그아웃
- 내 정보 조회
- 모집글 CRUD
- 본인 모집글만 수정/삭제
- 댓글/답글 CRUD
- 검색, 페이지네이션
- 카테고리 목록

### 2차 완료

- 좋아요
- 북마크
- 모집 상태 변경
- 이미지 업로드
- 조회수 중복 방지
- 아이디 찾기
- 이메일 인증번호 기반 비밀번호 재설정
- 로그인 5회 실패 시 10분 제한
- 마이룸 캐릭터 커스터마이징

### 3차 완료

- 관리자 숨김 페이지
- 회원 제재
- 모집글 관리자 삭제
- 댓글 관리자 삭제
- 관리자 검색
- 모집글/댓글 신고 및 신고 관리
- 관리자 운영 기록
- UI/UX 안정화
- WebSocket 실시간 접속 멤버
- 전체 사용자 흐름 통합 테스트

## 남은 운영 작업

- 포트폴리오에 프로젝트 2 진행 내용 반영
- 실제 SMTP 환경에서 인증번호 발송 확인
- 배포 환경의 DB, 파일 저장소, 관리자 환경변수 구성

## WebSocket

| Endpoint | 설명 | 인증 |
| --- | --- | --- |
| `/ws/presence` | 로그인 사용자의 실시간 접속/해제 및 온라인 멤버 목록 전송 | 세션 필요 |

서버 메시지 예시:

```json
{
  "type": "presence",
  "members": [
    { "id": 1, "nickname": "test1" }
  ]
}
```
