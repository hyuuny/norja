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

| <div style="width:200px"> **Method** |  <div style="width:300px">     **URI**  | <div style="width:290px"> **Description** |
|:------------------------------------:|:---------------------------------------:|:-----------------------------------------:|
|                 GET                  |        /api/v1/lodging-companies        |               숙박업체 조회 및 검색                |
|                 POST                 |        /api/v1/lodging-companies        |                  숙박업체 등록                  |
|                 GET                  |     /api/v1/lodging-companies/{id}      |                숙박업체 상세 조회                 |
|                 PUT                  |     /api/v1/lodging-companies/{id}      |                  숙박업체 수정                  |
|                 PUT                  | /api/v1/lodging-companies/vacation/{id} |                  숙박업체 휴무                  |
|                DELETE                |     /api/v1/lodging-companies/{id}      |                  숙박업체 삭제                  |

<br>

- 사용자

| <div style="width:200px"> **Method** | <div style="width:300px">     **URI** | <div style="width:290px"> **Description** |
|:------------------------------------:|:-------------------------------------:|:-----------------------------------------:|
|                 GET                  |       /api/v1/lodging-companies       |               숙박업체 조회 및 검색                |
|                 GET                  |    /api/v1/lodging-companies/{id}     |                숙박업체 상세 조회                 |

<br>

### Room (객실)

- 관리자

| <div style="width:200px"> **Method** | <div style="width:300px">     **URI** | <div style="width:290px"> **Description** |
|:------------------------------------:|:-------------------------------------:|:-----------------------------------------:|
|                 POST                 |             /api/v1/rooms             |                   객실 등록                   |
|                 GET                  |          /api/v1/rooms/{id}           |                 객실 상세 조회                  |
|                 PUT                  |          /api/v1/rooms/{id}           |                   객실 수정                   |
|                DELETE                |          /api/v1/rooms/{id}           |                   객실 삭제                   |

<br>


- 사용자


| <div style="width:200px"> **Method** | <div style="width:300px">     **URI** | <div style="width:290px"> **Description** |
|:------------------------------------:|:-------------------------------------:|:-----------------------------------------:|
|                 GET                  |          /api/v1/rooms/{id}           |                 객실 상세 조회                  |

<br>

### Reservation (예약)

- 관리자

| <div style="width:200px"> **Method** | <div style="width:300px">     **URI** | <div style="width:290px"> **Description** |
|:------------------------------------:|:-------------------------------------:|:-----------------------------------------:|
|                 GET                  |         /api/v1/reservations          |                예약 조회 및 검색                 |
|                 GET                  |       /api/v1/reservations/{id}       |                 예약 상세 조히                  |

<br>

- 사용자

| <div style="width:200px"> **Method** | <div style="width:300px">     **URI**  | <div style="width:290px"> **Description** |
|:------------------------------------:|:--------------------------------------:|:-----------------------------------------:|
|                 GET                  |          /api/v1/reservations          |                예약 조회 및 검색                 |
|                 POST                 |          /api/v1/reservations          |                   예약 등록                   |
|                 GET                  |       /api/v1/reservations/{id}        |                 예약 상세 조회                  |
|                DELETE                | /api/v1/reservations/cancellation/{id} |                   예약 취소                   |

<br>

### Review (후기)

- 관리자

| <div style="width:200px"> **Method** | <div style="width:300px">     **URI** | <div style="width:290px"> **Description** |
|:------------------------------------:|:-------------------------------------:|:-----------------------------------------:|
|                 GET                  |            /api/v1/reviews            |                후기 조회 및 검색                 |
|                 GET                  |         /api/v1/reviews/{id}          |                 후기 상세 조회                  |
|                DELETE                |         /api/v1/reviews/{id}          |                   후기 삭제                   |
|                 POST                 |         /api/v1/reviews/best          |               베스트 후기 선정/해제                |

<br>

- 사용자

| <div style="width:200px"> **Method** | <div style="width:300px">     **URI** | <div style="width:290px"> **Description** |
|:------------------------------------:|:-------------------------------------:|:-----------------------------------------:|
|                 GET                  |            /api/v1/reviews            |                후기 조회 및 검색                 |
|                 GET                  | /api/v1/reviews/lodging-company/{id}  |             숙박업체 후기 평균 점수 조회              |
|                 POST                 |            /api/v1/reviews            |                   후기 등록                   |
|                 GET                  |         /api/v1/reviews/{id}          |                 후기 상세 조회                  |
|                DELETE                |         /api/v1/reviews/{id}          |                   후기 삭제                   |

<br>

### Category (카테고리)

- 관리자

| <div style="width:200px"> **Method** |     <div style="width:300px">     **URI**      | <div style="width:290px"> **Description** |
|:------------------------------------:|:----------------------------------------------:|:-----------------------------------------:|
|                 POST                 |               /api/v1/categories               |                  카테고리 등록                  |
|                 POST                 |  /api/v1/categories/{parentCategoryId}/child   |                자녀 카테고리 등록                 |
|                 GET                  |               /api/v1/categories               |                카테고리 전체 조회                 |
|                 GET                  | /api/v1/categories/{parentCategoryId}/children |                자녀 카테고리 조회                 |
|                 GET                  |            /api/v1/categories/{id}             |                카테고리 상세 조회                 |
|                 PUT                  |            /api/v1/categories/{id}             |                  카테고리 수정                  |
|                DELETE                |            /api/v1/categories/{id}             |                  카테고리 삭제                  |

<br>

- 사용자

| <div style="width:200px"> **Method** |     <div style="width:300px">     **URI**      | <div style="width:290px"> **Description** |
|:------------------------------------:|:----------------------------------------------:|:-----------------------------------------:|
|                 GET                  |               /api/v1/categories               |                카테고리 전체 조회                 |
|                 GET                  | /api/v1/categories/{parentCategoryId}/children |                자녀 카테고리 조회                 |
|                 GET                  |            /api/v1/categories/{id}             |                카테고리 상세 조회                 |

<br>

### User (회원)

- 관리자

| <div style="width:200px"> **Method** | <div style="width:300px">     **URI** | <div style="width:290px"> **Description** |
|:------------------------------------:|:-------------------------------------:|:-----------------------------------------:|
|                 POST                 |             /api/v1/auth              |                    로그인                    |
|                 GET                  |             /api/v1/users             |                회원 조회 및 검색                 |
|                 GET                  |          /api/v1/users/{id}           |                 회원 상세 조회                  |

<br>

- 사용자

| <div style="width:200px"> **Method** |  <div style="width:300px">     **URI**  | <div style="width:290px"> **Description** |
|:------------------------------------:|:---------------------------------------:|:-----------------------------------------:|
|                 POST                 |              /api/v1/auth               |                    로그인                    |
|                 POST                 |          /api/v1/users/sign-up          |                   회원가입                    |
|                 GET                  |           /api/v1/users/{id}            |                 회원 상세 조회                  |
|                 POST                 |   /api/v1/users/{id}/change-password    |                  비밀번호 변경                  |
|                 POST                 | /api/v1/users/{id}/change-agreed |                 동의 여부 변경                  |
|                 PUT                  |           /api/v1/users/{id}            |                 회원 정보 변경                  |
|                DELETE                |           /api/v1/users/{id}            |                   회원 탈퇴                   |
