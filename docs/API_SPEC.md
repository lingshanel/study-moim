# 스터디모임 API 명세 v0.1

기본 prefix는 `/api`입니다.

인증이 필요한 API는 Session/Cookie 기반 인증을 사용하는 방향으로 시작합니다.

## Auth / 회원

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/api/auth/signup` | 회원가입 | - |
| POST | `/api/auth/login` | 로그인 | - |
| POST | `/api/auth/logout` | 로그아웃 | 필요 |
| GET | `/api/users/me` | 내 정보 조회 | 필요 |
| PATCH | `/api/users/me` | 내 정보 수정 | 필요 |
| PATCH | `/api/users/me/password` | 비밀번호 변경 | 필요 |
| DELETE | `/api/users/me` | 회원 탈퇴 | 필요 |

## Categories

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| GET | `/api/categories` | 카테고리 목록 조회 | - |

## Study Posts

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/api/study-posts` | 모집글 작성 | 필요 |
| GET | `/api/study-posts` | 모집글 목록 조회 | - |
| GET | `/api/study-posts/{postId}` | 모집글 상세 조회 | - |
| PATCH | `/api/study-posts/{postId}` | 모집글 수정 | 작성자 또는 ADMIN |
| DELETE | `/api/study-posts/{postId}` | 모집글 삭제 | 작성자 또는 ADMIN |
| PATCH | `/api/study-posts/{postId}/status` | 모집 상태 변경 | 작성자 또는 ADMIN |

목록 조회 query:

| Query | 설명 |
| --- | --- |
| page | 페이지 번호 |
| size | 페이지 크기 |
| category | 카테고리 slug |
| keyword | 제목/내용 검색어 |
| studyType | ONLINE, OFFLINE, HYBRID |
| status | RECRUITING, CLOSED |
| sort | latest 등 |

## Comments

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/api/study-posts/{postId}/comments` | 댓글 작성 | 필요 |
| GET | `/api/study-posts/{postId}/comments` | 댓글 목록 조회 | - |
| PATCH | `/api/comments/{commentId}` | 댓글 수정 | 작성자 또는 ADMIN |
| DELETE | `/api/comments/{commentId}` | 댓글 삭제 | 작성자 또는 ADMIN |

## Likes

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/api/study-posts/{postId}/likes` | 좋아요 추가 | 필요 |
| DELETE | `/api/study-posts/{postId}/likes` | 좋아요 취소 | 필요 |

## Bookmarks

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/api/study-posts/{postId}/bookmarks` | 북마크 추가 | 필요 |
| DELETE | `/api/study-posts/{postId}/bookmarks` | 북마크 취소 | 필요 |
| GET | `/api/users/me/bookmarks` | 내 북마크 목록 | 필요 |

## Images

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/api/images` | 이미지 업로드 | 필요 |
| DELETE | `/api/images/{imageId}` | 이미지 삭제 | 업로드 사용자 또는 ADMIN |

## Admin

| Method | Path | 설명 | 인증 |
| --- | --- | --- | --- |
| GET | `/api/admin/users` | 회원 목록 조회 | ADMIN |
| PATCH | `/api/admin/users/{userId}/status` | 회원 상태 변경 | ADMIN |
| DELETE | `/api/admin/study-posts/{postId}` | 게시글 관리자 삭제 | ADMIN |
| DELETE | `/api/admin/comments/{commentId}` | 댓글 관리자 삭제 | ADMIN |

## MVP 우선순위

### 1차

- 회원가입
- 로그인 / 로그아웃
- 내 정보 조회
- 게시글 CRUD
- 본인 게시글만 수정/삭제
- 댓글 CRUD
- 검색
- 페이지네이션
- 카테고리 목록

### 2차

- 좋아요
- 북마크
- 모집 상태 변경
- 비밀번호 변경
- 회원 탈퇴

### 3차

- 이미지 업로드
- 관리자 기능
- 로그인 실패 제한
