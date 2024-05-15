## 🌊 챌린지의 파도를 넘어 취업으로, WAVED-BE  


### 📆 개발기간
- 개발 : 2024년 2월 26일 ~ 2024년 3월 22일 
- 테스트 및 리팩토링 : 2024년 4일 1일 ~ 2024년 4월 26월 

### 🔧 Tools

<div align=left> 
  <img src="https://img.shields.io/badge/JAVA 17-407999?style=for-the-badge&logo=JAVA 17&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"> 
  <img src="https://img.shields.io/badge/OAuth-43853D?style=flat-square&logo=OAuth&logoColor=white"/>
  <img src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=JSONWebTokens&logoColor=white"/>
  <img src="https://img.shields.io/badge/lombok-C02E18?style=for-the-badge&logo=lombok&logoColor=white">
  <br>    
  
  <img src="https://img.shields.io/badge/SpringDataJPA-53B421?style=for-the-badge&logo=SpringDataJPA&logoColor=white">
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
  <img src="https://img.shields.io/badge/azure-0078D4?style=for-the-badge&logo=microsoft-azure&logoColor=white">
  <img src="https://img.shields.io/badge/azure%20storage-0089D6?style=for-the-badge&logo=microsoft-azure&logoColor=white">
  <br>  

</div>

### 🏄‍♀️ 아키텍쳐
![waved-BE-Page-1](https://i.ibb.co/CM6FNjz/waved-BE-Page-1.jpg)

### 🏄‍♀️ API명세  
- 포스트맨 : https://documenter.getpostman.com/view/34016201/2sA3JRXds2     

|  | Method | URI | Description |
| --- | --- | --- | --- |
| 관리자 | GET | /api/v2/admin/groups | 진행중인 챌린지 그룹 조회 |
|  | GET | /api/v2/admin/{challengeGroupId}/verifications | 그룹별 인증 내역 조회 |
|  | DELETE | /api/v2/admin/{challengeGroupId}/verifications/{verificationId} | 인증 내역 취소 논리삭제 |
| 일반회원 | PATCH | /api/v1/members/edit | 멤버 정보 수정(초기 회원 가입시) |
|  | POST | /api/v1/members/reissue | 토큰 재발급 refreshTocken 활용 |
|  | POST | /api/v1/members/logout | 로그아웃 accessToken 만료 기간 전인 10분동안은 블랙리스트 처리 |
|  | DELETE | /api/v1/members/delete | 회원 탈퇴 |
|  | GET | /api/v1/members/profile | 프로필 조회 |
|  | GET | /api/v1/members/profile/edit | 프로필 수정 |
|  | POST | /api/v1/members/github | 깃허브 연동 |
|  | GET | /api/v1/members/github | 깃허브 정보 조회 |
|  | DELETE | /api/v1/members/github | 깃허브 연동 해제 |
|  | GET | /api/v1/members/reviews?page=0&limit=5 | 자신의 리뷰 조회 5개씩 페이징 |
|  | GET | /api/v1/members/paymentRecords?page=0&limit=5 | 예치금 내역 조회5개씩 페이징 |
| 챌린지 | GET | /api/v1/challenges/waiting | 대기중인 전체 챌린지 조회 비회원조회가능 |
|  | GET | /api/v1/challenges/{challengeId}/reviews?page=0&limit=5 | 챌린지 전체 리뷰 조회 5개씩 페이징 비회원조회가능 |
| 챌린지 그룹(기수) | GET | /api/v1/challengeGroups/info/{challengeGroupId} | 챌린지 그룹 상세 정보 조회 비회원조회가능 |
|  | POST | /api/v1/challengeGroups/{challengeGroupId}?deposit= | 챌린지 그룹 신청 |
|  | GET | /api/v1/challengeGroups/{challengeGroupId}?verificationDate= | 챌린지 그룹의, 날짜별, 인증 내역 조회 |
|  | GET | /api/v1/challengeGroups/{challengeGroupId}/myVerifies?verificationDate= | 챌린지 그룹의 내 인증 내역 조회 |
| 마이챌린지 | GET | /api/v1/myChallenges?status=PROGRESS | WAITING | COMPLETED
| 상태별 마이 챌린지 조회 |
|  | GET | /api/v1/myChallenges/{myChallengeId} | 나의(마이챌린지) 인증 내역 조회 |
| 인증 | POST | /api/v1/verify/{challengeGroupId}
→ form-data : imageUrl, content, link, 깃헙은 제출 x | 챌린지 인증 제출 (글, 링크, 사진, 깃허브) 중복 제출 불가능 |
|  | GET | /api/v1/verify/{challengeGroupId}/dates?quizDate=2024-03-20 03:25:02.337779 | 그룹의, 날짜별 퀴즈 조회 |
|  | GET | /api/v1/verify/{challengeGroupId} | 그룹의, 오늘의 퀴즈 조회 |
| 리뷰 | POST | /api/v1/reviews/myChallenge/{myChallengeId} | 리뷰 등록 중복 등록 불가능 |
|  | GET | /api/v1/reviews/{reviewId} | 리뷰 수정용 content 조회 |
|  | PATCH | /api/v1/reviews/{reviewId} | 리뷰 수정 |
|  | DELETE | /api/v1/reviews/{reviewId} | 리뷰 삭제 |
| 결제 | POST | /api/v1/payments/{myChallengeId} | 결제 후 검증 |
|  | POST | /api/v1/payments/{myChallengeId}/cancel | 챌린지 신청 취소 시 환급 |
|  | POST | /api/v1/payments/{myChallengeId}/completed | 챌린지 완주 시 환불 버튼 |
| 좋아요 | POST | /api/v1/likes/{verificationId} | 좋아요 추가 중복 추가 불가능 |
|  | GET | /api/v1/likes/{verificationId} | 특정 인증 내역 좋아요 수 조회 |
|  | DELETE | /api/v1/likes/{verificationId} | 좋아요 취소 |
| 알림 | GET | /api/v1/event/subscribe | 알림 구독 |
|  | GET | /api/v1/event/new | 새로운 알림 유무 확인 |

### 🏄‍♀️ 테이블 (다이어그램)  
<img width="700" alt="스크린샷 2023-12-21 오후 2 08 32" src="https://i.ibb.co/djSMQCh/image.png"> 


### 🏄‍♀️ 사이트
https://waved-likelion.site/

### 🏄‍♀️ 구현 기능  
#### 1. 회원가입/로그인  
- 스프링 시큐리티와 OAuth 2.0 로그인 연동을 JWT 토큰 기반으로 구현하였고, Redis를 사용하여 refresh Token을 저장하였습니다.
- 로그아웃한 유저 토큰의 블랙리스트 처리로 보안을 강화했습니다.
<br>  
<img width="700" alt="image" src="https://i.ibb.co/T0CQGSn/image.jpg">

#### 2. 실시간 알림
- 스프링 서버 사이드 이벤트 SseEmitter로 구현하였고, 관리자 단에서 취소되는 인증에 대해 유저에게 확실하게 전달할 수 있도록 실시간 알림을 사용했습니다.
<br>  
<img width="300" alt="image" src="https://i.ibb.co/qRjhnjG/ezgif-com-video-to-gif-converter.gif">

#### 3. 결제 사후 검증, 취소, 환급
- 포트원과 연동하여 결제 요청을 확인하고, 금액이 정확한지 체크하는 사후 검증 과정과 결제 취소 및 환급 과정의 전체 결제 프로세스를 구현했습니다.
  <br>  
<img width="400" alt="image" src="https://i.ibb.co/9ZZZ76t/waved-ezgif-com-video-to-gif-converter.gif">

### 🏄‍♀️ 패키지 구조
```

└── src
   ├── main
      ├── java
      │   └── com
      │      └── senity
      │          └── waved
      │             ├── base
      │	            │  ├── config
      │	            │  ├── exception
      │	            │  ├── jwt
      │	            │  ├── redis
      │	            │  └── security
      │	            ├── common
      │	            │	
      │	            │
      │	            └── domain
      │                  ├─── amdin
      │                       ├─── controller
      │                       └─── service
      │                  ├─── challenge
      │                       ├─── controller
      │                       ├─── entity
      │                       ├─── exception
      │                       ├─── repository
      │                       └─── service
      │                  ├─── challengeGroup
      │                       ├─── controller
      │                       ├─── dto
      │                            └─── response
      │                       ├─── entity
      │                       ├─── exception
      │                       ├─── repository
      │                       └─── service
      │                  ├─── event
      │                       ├─── controller
      │                       ├─── repository
      │                       └─── service
      │                  ├─── liked
      │                       ├─── controller
      │                       ├─── dto
      │                            └─── response
      │                       ├─── entity
      │                       ├─── exception
      │                       ├─── repository
      │                       └─── service	
      │                  ├─── member
      │                       ├─── controller
      │                       ├─── dto
      │                            ├─── request
      │                            └─── response
      │                       ├─── entity
      │                       ├─── exception
      │                       ├─── repository
      │                       └─── service
      │                  ├─── myChallenge
      │                       ├─── controller
      │                       ├─── dto
      │                            └─── response
      │                       ├─── entity
      │                       ├─── exception
      │                       ├─── repository
      │                       └─── service
      │                  ├─── notification
      │                       ├─── controller
      │                       ├─── dto
      │                            └─── response
      │                       ├─── entity
      │                       ├─── repository
      │                       └─── service
      │                  ├─── paymentRecord
      │                       ├─── controller
      │                       ├─── dto
      │                            ├─── request
      │                            └─── response
      │                       ├─── entity
      │                       ├─── exception
      │                       ├─── repository
      │                       └─── service
      │                  ├─── quiz
      │                       ├─── controller
      │                       ├─── dto
      │                            └─── response
      │                       ├─── entity
      │                       ├─── exception
      │                       ├─── repository
      │                       └─── service
      │                  ├─── review
      │                       ├─── controller
      │                       ├─── dto
      │                            └─── response
      │                       ├─── entity
      │                       ├─── exception
      │                       ├─── repository
      │                       └─── service
      │                  ├─── verification
      │                       ├─── controller
      │                       ├─── dto
      │                            ├─── request
      │                            └─── response
      │                       ├─── entity
      │                       ├─── exception
      │                       ├─── repository
      │                       └─── service
      │              
      │                           
      └── resource
              ├── application.yml
              └── application-secret.yml

``` 
