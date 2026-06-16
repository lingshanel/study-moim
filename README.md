# 스터디모임

스터디모임은 스터디를 원하는 사용자가 모집글을 작성하고, 댓글과 답글로 참여 의사를 나눌 수 있는 스터디 모집 게시판입니다.

AWOO/KR보다 더 큰 서비스를 만들기보다는, 신입 백엔드 취업 준비에서 기본기를 보여줄 수 있는 정석적인 게시판 서비스를 목표로 만들었습니다. Java와 Spring Boot를 사용해 회원, 인증, 게시글, 댓글, 검색, 페이지네이션, 반응, 신고, 관리자 기능을 직접 구현해보며 웹 서비스의 기본 흐름을 연습했습니다.

## 배포

- Live Demo: https://study-moim-production.up.railway.app
- Admin Page: `/ops/study-moim-admin`
- GitHub: https://github.com/lingshanel/study-moim

관리자 계정은 공개 문서에 비밀번호를 적지 않습니다. 최초 배포 시 Railway 환경변수로 생성한 뒤, 생성 확인 후 `ADMIN_ENABLED=false`로 전환합니다.

## 기술 스택

| 영역 | 사용 기술 |
| --- | --- |
| Backend | Java 21, Spring Boot, Spring MVC, Spring Security |
| Database | MySQL, Spring Data JPA, Hibernate |
| Frontend | HTML, CSS, Vanilla JavaScript |
| Build | Gradle |
| Deploy | Railway, Docker |
| Storage | Local File Upload, Railway Volume |

## 주요 기능

### 회원 / 인증

- 닉네임과 비밀번호 기반 로그인
- 회원가입, 로그아웃, 내 정보 조회
- 이메일 기반 아이디 찾기
- 이메일 인증번호 기반 비밀번호 재설정
- 로그인 5회 실패 시 10분간 로그인 제한
- 비밀번호 변경, 회원 탈퇴
- 비로그인 사용자의 글쓰기, 댓글, 좋아요, 북마크 접근 제한

### 모집글

- 모집글 작성, 조회, 수정, 삭제
- 작성자 본인만 수정/삭제 가능
- 카테고리, 제목/내용 검색
- 진행 방식 필터: 온라인, 오프라인, 혼합
- 모집 상태 필터: 모집중, 모집마감
- 최신순, 좋아요순, 북마크순 정렬
- 페이지네이션
- 상세 조회 시 같은 브라우저 세션 기준 조회수 1회 증가
- 이미지 업로드 및 상세 화면 원본 이미지 보기

### 댓글 / 답글

- 댓글 작성, 조회, 수정, 삭제
- 답글 작성
- 댓글 수정 시 기존 내용 불러오기
- 댓글 작성자의 캐릭터 표시
- 댓글 삭제 확인 모달 적용

### 반응 / 상태

- 좋아요 추가/취소
- 북마크 추가/취소
- 모집중/모집마감 상태 변경
- 연속 클릭 방지를 위한 버튼 쿨타임 적용
- 버튼 상태 변경 이펙트 적용

### UI / UX

- 싸이월드 미니홈피 감성과 Discord식 카테고리 구조를 섞은 화면 구성
- 좌측 카테고리, 중앙 모집글, 우측 접속 멤버/추천 기능/로드맵 구조
- MY ROOM 캐릭터 커스터마이징
- WebSocket 기반 실시간 접속 멤버 표시
- 상세 모달 내부 스크롤 분리
- 로그인 상태에 따라 메뉴와 권한 버튼 노출 변경

### 신고 / 관리자

- 모집글 신고
- 댓글/답글 신고
- 본인 글/댓글 신고 버튼 숨김
- 중복 신고 방지
- 별도 관리자 페이지 운영
- 회원 검색 및 제재
- 제재 사유 저장 및 로그인 시 안내
- 모집글/댓글 검색, 미리보기, 관리자 삭제
- 신고 목록 검색 및 처리 완료
- 관리자 작업 로그 조회

## 문서

- [ERD](./docs/ERD.md)
- [API 명세](./docs/API_SPEC.md)
- [디자인 방향](./docs/DESIGN_THEME.md)
- [트러블슈팅](./docs/TROUBLESHOOTING.md)

## 로컬 실행

MySQL 데이터베이스를 준비합니다.

```sql
CREATE DATABASE study_moim;
```

환경변수 예시:

```text
DB_URL=jdbc:mysql://localhost:3306/study_moim?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
DB_USERNAME=study_moim
DB_PASSWORD=study_moim_dev

MAIL_ENABLED=false

ADMIN_ENABLED=false
```

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

## Railway 배포 설정

애플리케이션 서비스 환경변수:

```text
DB_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
DB_USERNAME=${{MySQL.MYSQLUSER}}
DB_PASSWORD=${{MySQL.MYSQLPASSWORD}}
SESSION_COOKIE_SECURE=true
UPLOAD_DIR=/data/uploads
MAIL_ENABLED=false
ADMIN_ENABLED=false
```

최초 관리자 계정 생성 시에만 아래 값을 추가하고 `ADMIN_ENABLED=true`로 배포합니다. 계정 생성 확인 후 다시 `ADMIN_ENABLED=false`로 변경합니다.

```text
ADMIN_EMAIL=admin@example.com
ADMIN_NICKNAME=superadmin
ADMIN_PASSWORD=change-this-password
```

Railway 서비스 설정:

- Healthcheck Path: `/api/health`
- Public Networking: Generate Domain
- Volume Mount Path: `/data`

## 검증

- Spring Boot 테스트 통과
- JavaScript 문법 점검 완료
- 공개 배포 주소 응답 확인
- `/api/health` 200 OK 확인
- `/api/categories`, `/api/study-posts` 공개 API 응답 확인
- 포트폴리오 Live Demo 링크 반영 확인

## 현재 진행 상태

- 1차 MVP: 회원, 로그인, 모집글 CRUD, 댓글, 검색, 페이지네이션 구현 완료
- 2차 기능: 좋아요, 북마크, 모집 상태, 이미지 업로드, 캐릭터 커스터마이징 구현 완료
- 관리자/보안: 신고, 회원 제재, 관리자 삭제, 로그인 실패 제한, 비밀번호 재설정 구현 완료
- 배포: Railway + MySQL + Volume + Healthcheck 구성 완료

## 이후 개선할 점

- 실제 SMTP 환경에서 이메일 인증번호 발송 검증
- 관리자 작업 로그의 검색 조건 추가
- 이미지 저장소를 S3 같은 외부 Object Storage로 확장
- 테스트 케이스를 더 세분화해 예외 상황 보강
