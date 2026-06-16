# 스터디모임 API 명세

스터디모임에서 구현한 REST API를 정리한 문서입니다.

- 인증 방식: Session/Cookie
- 응답 형식: JSON
- 로컬 서버: `http://localhost:8080`
- 배포 서버: `https://study-moim-production.up.railway.app`
- 관리자 페이지: `/ops/study-moim-admin`

## 공통 오류 응답

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
| POST | `/api/auth/password-reset/verify` | 인증번호 확인 및 재설정 토큰 발급 | - |
| POST | `/api/auth/password-reset/confirm` | 새 비밀번호 설정 | - |

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
| `keyword` | 제목/내용 검색어 | `Spring` |
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
| GET | `/api/study-posts/{postId}/comments` | 특정 모집글 댓글 조회 | - |
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
| PATCH | `/api/admin/reports/{reportId}/resolve` | 신고 처리 완료 | ADMIN |

신고 규칙:

- 로그인한 사용자만 신고할 수 있습니다.
- 본인이 작성한 모집글이나 댓글은 신고할 수 없습니다.
- 삭제된 댓글은 신고할 수 없습니다.
- 같은 사용자는 같은 대상에 대해 미처리 신고를 중복 등록할 수 없습니다.

## Admin

관리자 기능은 일반 사용자 메뉴에 노출하지 않고, 별도 주소로 접근합니다.

```text
/ops/study-moim-admin
```

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| GET | `/api/admin/users` | 회원 목록/검색 | ADMIN |
| PATCH | `/api/admin/users/{userId}/status` | 회원 상태 변경 및 제재 사유 저장 | ADMIN |
| DELETE | `/api/admin/study-posts/{postId}` | 모집글 관리자 삭제 | ADMIN |
| DELETE | `/api/admin/comments/{commentId}` | 댓글 관리자 삭제 | ADMIN |
| GET | `/api/admin/reports` | 신고 목록/검색 | ADMIN |
| PATCH | `/api/admin/reports/{reportId}/resolve` | 신고 처리 완료 | ADMIN |
| GET | `/api/admin/action-logs` | 관리자 작업 로그 조회 | ADMIN |

## WebSocket

| Endpoint | 설명 | 인증 |
| --- | --- | --- |
| `/ws/presence` | 로그인 사용자의 실시간 접속/해제 및 온라인 멤버 목록 전송 | 세션 필요 |

서버 메시지 예시:

```json
{
  "type": "presence",
  "members": [
    {
      "id": 1,
      "nickname": "test1"
    }
  ]
}
```
