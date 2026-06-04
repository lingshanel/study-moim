# 스터디모임

스터디모임은 스터디를 원하는 사람이 모집 게시글을 작성하고, 관심 있는 사용자가 댓글이나 북마크로 참여 의사를 표현할 수 있는 스터디 모집 플랫폼입니다.

아직 배워가는 입장에서 Java/Spring 기반 백엔드 기본 기능을 차분히 만들어보기 위한 프로젝트입니다.

## 목표

- Java와 Spring Boot로 REST API 만들어보기
- Spring MVC 구조를 사용해 controller, service, repository 역할 나누어보기
- Spring Security와 Session/Cookie 기반 인증 흐름 공부하기
- MySQL과 Spring Data JPA를 사용해 관계형 DB 다뤄보기
- 게시글, 댓글, 검색, 페이지네이션 같은 기본 기능 구현해보기

## 기술 스택

- Java 21
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA / Hibernate
- MySQL
- Gradle
- HTML, CSS, Vanilla JavaScript
- Local file upload

## 기능 범위

### 1차 MVP

- 회원가입
- 로그인 / 로그아웃
- 내 정보 조회
- 스터디 모집글 CRUD
- 본인 게시글만 수정/삭제
- 댓글 CRUD
- 카테고리 목록
- 검색
- 페이지네이션

### 이후 확장

- 좋아요
- 북마크
- 모집 상태 변경
- 비밀번호 변경
- 회원 탈퇴
- 이미지 업로드
- 관리자 기능
- 로그인 실패 제한

## 문서

- [ERD 초안](./docs/ERD.md)
- [API 명세 v0.1](./docs/API_SPEC.md)

## 로컬 실행 준비

이 프로젝트는 MySQL을 사용합니다. 로컬에서 실행하기 전에 DB를 먼저 준비합니다.

```sql
CREATE DATABASE study_moim;
```

환경변수 예시:

```text
DB_URL=jdbc:mysql://localhost:3306/study_moim?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
DB_USERNAME=root
DB_PASSWORD=your-password
```

실행:

```bash
./gradlew bootRun
```

Windows:

```powershell
.\gradlew.bat bootRun
```

## 현재 상태

초기 Spring Boot 프로젝트 세팅 단계입니다. 먼저 회원, 인증, 게시글, 댓글 기능부터 작게 시작할 예정입니다.
