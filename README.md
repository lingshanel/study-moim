# 스터디모임

스터디모임은 스터디를 원하는 사용자가 모집글을 작성하고, 댓글과 답글로 참여 의사를 나눌 수 있는 스터디 모집 게시판입니다.

AWOO/KR보다 규모를 크게 잡기보다는, 신입 백엔드 취업 준비에서 기본기를 보여줄 수 있는 정석적인 CRUD 서비스로 만들었습니다. Java/Spring 기반으로 회원과 세션 인증, 모집글, 댓글과 답글, 검색, 페이지네이션, 반응, 신고와 관리자 기능을 구현했습니다.

## 목표

- Spring MVC 기반 REST API 흐름을 직접 구현해보기
- Controller, Service, Repository 계층을 나누어 백엔드 구조 익히기
- Session/Cookie 기반 인증 흐름 적용하기
- MySQL과 Spring Data JPA로 관계형 데이터 다뤄보기
- 게시글, 댓글, 검색, 페이지네이션, 이미지 업로드 같은 웹 서비스 기본 기능 구현하기
- 싸이월드 느낌의 개인 공간 UI와 디스코드식 커뮤니티 구조를 섞어보기

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

## 현재 구현된 기능

### 회원 / 인증

- 회원가입
- 닉네임 + 비밀번호 로그인
- 이메일로 아이디 찾기
- 이메일 인증번호 확인 후 비밀번호 재설정
- 로그인 5회 실패 시 10분간 계정 로그인 제한
- 로그아웃
- 내 정보 조회
- 비밀번호 변경
- 회원 탈퇴
- 로그인하지 않은 사용자의 글쓰기, 좋아요, 북마크, 댓글 작성 제한

### 모집글

- 모집글 작성, 조회, 수정, 삭제
- 작성자 본인만 수정/삭제 가능
- 상세 조회 시 같은 브라우저 세션에서는 조회수 1회 증가
- 카테고리별 목록 조회
- 제목/내용 검색
- 진행 방식 필터: `ONLINE`, `OFFLINE`, `HYBRID`
- 모집 상태 필터: `RECRUITING`, `CLOSED`
- 정렬: 최신순, 좋아요순, 북마크순
- 페이지네이션
- 대표 이미지 로컬 업로드
- 상세 화면에서 이미지 원본 보기

### 댓글 / 답글

- 댓글 작성, 조회, 수정, 삭제
- 댓글 수정 시 기존 내용 불러오기
- 삭제 확인 모달 적용
- 답글 작성
- 답글 작성 후 바로 보이도록 스크롤/하이라이트 처리
- 댓글 작성자의 캐릭터 표시

### 반응 / 상태

- 좋아요 추가/취소
- 북마크 추가/취소
- 모집중/모집마감 상태 변경
- 반응 버튼 연속 클릭 방지를 위한 짧은 쿨타임
- 버튼 상태 변경 이펙트

### UI / UX

- 싸이월드 기반 미니룸 느낌의 화면 구성
- 카테고리 탐색 영역
- 우측 접속 멤버 영역
- WebSocket 기반 실시간 접속 멤버 표시
- MY ROOM 캐릭터 표시
- 캐릭터 커스터마이징
- 글 상세 모달 내부 스크롤 분리
- 테마에 맞춘 확인 모달, 댓글 편집 UI

### 신고 / 관리자

- 본인 콘텐츠를 제외한 모집글·댓글 신고
- 동일 대상에 대한 미처리 신고 중복 방지
- 숨겨진 관리자 페이지와 별도 관리자 로그인
- 회원 검색 및 제재 사유가 포함된 상태 변경
- 모집글·댓글 검색, 미리보기, 관리자 삭제
- 신고 검색 및 처리 완료
- 관리자 작업 기록 조회

## 문서

- [ERD](./docs/ERD.md)
- [API 명세](./docs/API_SPEC.md)
- [디자인 테마](./docs/DESIGN_THEME.md)
- [트러블슈팅](./docs/TROUBLESHOOTING.md)

## 로컬 실행 준비

MySQL 데이터베이스를 준비합니다.

```sql
CREATE DATABASE study_moim;
```

환경변수 예시:

```text
DB_URL=jdbc:mysql://localhost:3306/study_moim?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
DB_USERNAME=study_moim
DB_PASSWORD=study_moim_dev

# 실제 이메일 인증번호 발송을 사용할 때
MAIL_ENABLED=true
MAIL_HOST=smtp.example.com
MAIL_PORT=587
MAIL_USERNAME=your-account
MAIL_PASSWORD=your-password
MAIL_SMTP_AUTH=true
MAIL_STARTTLS=true
MAIL_FROM=no-reply@example.com

# 최초 슈퍼 관리자 계정을 생성할 때만 사용
ADMIN_ENABLED=true
ADMIN_EMAIL=admin@example.com
ADMIN_NICKNAME=superadmin
ADMIN_PASSWORD=change-this-password
```

`MAIL_ENABLED=false`인 로컬 개발 환경에서는 비밀번호 재설정 인증번호가 서버 콘솔에 출력됩니다.

관리자 자동 생성은 기본적으로 꺼져 있습니다. 최초 생성 시에만 `ADMIN_ENABLED=true`와 관리자 환경변수를 설정하고, 계정이 생성된 뒤에는 다시 비활성화합니다.

실행:

```powershell
.\gradlew.bat bootRun
```

접속:

```text
http://localhost:8080/
```

테스트:

```powershell
.\gradlew.bat test
```

## 검증

- Spring Boot 통합 테스트 28개 통과
- 가입부터 모집글 작성, 조회, 좋아요·북마크, 댓글·답글, 신고, 관리자 처리까지 연결한 사용자 흐름 테스트 포함
- JavaScript 문법 검사 완료

## 현재 진행도

- 1차 MVP: 회원가입, 로그인/로그아웃, 게시글 CRUD, 댓글 CRUD, 검색, 페이지네이션 구현
- 2차 기능: 좋아요, 북마크, 모집 상태 변경, 비밀번호 변경, 비밀번호 재설정, 회원 탈퇴 구현
- 3차 기능: 이미지 업로드, 관리자 기능, 신고 관리, 운영 기록, 로그인 실패 제한 구현
- 현재 작업 단계: 핵심 기능 구현, 전체 사용자 흐름 점검, 문서와 포트폴리오 반영 완료 후 Railway 배포 준비 중

## 다음 예정 작업

- Railway 서비스와 MySQL 연결
- 공개 도메인에서 WebSocket, 세션, 이미지 업로드 최종 확인
- 실제 SMTP 메일 발송 환경에서 비밀번호 재설정 확인

## Railway 배포

이 프로젝트는 Dockerfile을 사용해 Railway에 배포할 수 있습니다.

1. Railway에서 GitHub 저장소 `lingshanel/study-moim`을 연결합니다.
2. 같은 프로젝트에 MySQL 서비스를 추가합니다.
3. 애플리케이션 서비스에 다음 환경변수를 설정합니다.

```text
DB_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
DB_USERNAME=${{MySQL.MYSQLUSER}}
DB_PASSWORD=${{MySQL.MYSQLPASSWORD}}
SESSION_COOKIE_SECURE=true

ADMIN_ENABLED=true
ADMIN_EMAIL=관리자 이메일
ADMIN_NICKNAME=관리자 닉네임
ADMIN_PASSWORD=충분히 긴 관리자 비밀번호
```

최초 배포에서 관리자 생성이 확인되면 `ADMIN_ENABLED=false`로 변경합니다.

이미지 파일을 재배포 후에도 유지하려면 애플리케이션 서비스에 Volume을 추가하고 `/data`에 마운트합니다. 애플리케이션은 기본적으로 `/data/uploads`에 이미지를 저장합니다.

Railway 서비스 설정:

- Healthcheck path: `/api/health`
- Public Networking: Generate Domain
- Mail을 사용하지 않을 때: `MAIL_ENABLED=false`
