# norja

> 코틀린을 학습하면서 이전부터 만들어보고 싶던 숙박 예약 시스템 API를 구현해보았습니다.

<br>

## 사용 기술

- `Kotlin`
- `Spring Boot 2.6.6`
- `MySQL 5.7`
- `H2`
- `JPA 1.6.21`
- `Querydsl 5.0.0`
- `Spring Security`
- `jjwt 0.9.1`
- `Redis`
- `docker-compose`
- `OpenAPI 3.0`
- `kotlin-logging:1.12.5`
- `Gradle`

<br>

## 실행

- docker-compose 컨테이너 생성

```angular2html
docker compose up -d
```

<br>

- 관리자 Swagger 접속
    1. NorjaAdminApplication 실행
    2. `localhost:8081/swagger-ui.html` 접속

<br>

- 사용자 Swagger 접속
    1. NorjaApplication 실행
    2. `localhost:8081/swagger-ui.html` 접속
    3. 회원가입 -> 로그인 후, `accessToken` 값 상단의 `Authorize`에 입력

```json
{
  "token": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYiLCJ1c2VybmFtZSI6Imh5dXVueUBrbm91LmFjLmtyIiwiZXhwIjoxNjYzMTMyNDc5fQ.FaceuQijnJtyR3w8moqbS_s49tZROwtBAD5eWIWsNMU"
  }
}
```

<br>

## 구조

![185549390-4ed1dad0-9b47-4798-81ac-de808a68a8c3](https://user-images.githubusercontent.com/69466533/190064783-cb3ebdeb-b66a-4935-a975-b00bffbc94b0.png)

<br>

## 기능

### LodgingCompany (숙박 업체)

- 관리자
    - 조회 및 검색
    - 등록
    - 상세 조회
    - 수정
    - 휴무 처리
    - 삭제


- 사용자
    - 조회 및 검색
    - 상세 조회

### Room (객실)

- 관리자
    - 등록
    - 상세 조회
    - 수정
    - 삭제


- 사용자
    - 상세 조회

### Reservation (예약)

- 관리자
    - 조회 및 검색
    - 상세 조회


- 사용자
    - 조회 및 검색
    - 예약 등록
    - 상세 조회
    - 예약 취소

### Review (후기)

- 관리자
    - 조회 및 검색
    - 상세 조회
    - 삭제
    - 베스트 후기 선정/해제


- 사용자
    - 조회 및 검색
    - 숙박 업체 후기 평균 점수 조회
    - 등록
    - 상세 조회
    - 삭제

### Category (카테고리)

- 관리자
    - 부모/자녀 카테고리 등록
    - 전체 조회
    - 자녀 카테고리 조회
    - 상세 조회
    - 수정
    - 삭제


- 사용자
    - 전체 조회
    - 자녀 카테고리 조회
    - 상세 조회

### User (회원)

- 관리자
    - 로그인
    - 조회 및 검색
    - 상세 조회


- 사용자
    - 로그인
    - 회원가입
    - 상세 조회
    - 비밀번호 변경
    - 동의 여부 변경
    - 정보 변경
    - 회원 탈퇴
