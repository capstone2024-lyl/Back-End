# 아이카드(I-Card, 나를 알고 소개하다)
## 프로젝트 소개

### 프로젝트 목적

- 코로나 시기 MBTI가 유행하며 여러 문답을 통해 자신의 성격을 특정 심볼(꽃, 색깔, 음식 등)에 비유하는 ‘레이블링 게임’이 인기인 것에서 착안
- ‘자신이 누구인가’라는 질문에 대한 탐구 과정으로 내가 어떤 사람인지를 인지하고 남에게 소개하는 것이 하나의 트렌드화
- 짧은 설문을 통한 본인 파악은 온전한 자신을 나타내기엔 부족한 점이 많기 때문에 스마트폰 사용 내역(SNS, 카메라, 어플 사용 시간)등을 통한 자기소개 카드 제작으로 본인을 파악하고 소개할 수 있도록 서비스 제공


### 프로젝트 구현
<img width="166" alt="캡 홈화면" src="https://github.com/capstone2024-lyl/Back-End/assets/63050966/4526d9a9-e885-42a8-ba36-307da363647e">
<img width="166" alt="캡 메인화면" src="https://github.com/capstone2024-lyl/Back-End/assets/63050966/09399464-3e60-4d82-969b-a77008375450">
<img width="166" alt="캡 채팅 분석" src="https://github.com/capstone2024-lyl/Back-End/assets/63050966/a18c7db9-807b-44a5-81a9-b4edeb0583f8">
<img width="166" alt="캡 어플 사용시간" src="https://github.com/capstone2024-lyl/Back-End/assets/63050966/01739017-7201-4db6-b408-445960e99140">


<img width="166" alt="캡 유튜브 구독목록" src="https://github.com/capstone2024-lyl/Back-End/assets/63050966/5a96b368-f0ab-4445-9232-366f94df7c0e">
<img width="166" alt="아이카드 앞면" src="https://github.com/capstone2024-lyl/Back-End/assets/63050966/1e12ab92-46a6-4a51-85f7-2e4eafecb188">
<img width="166" alt="아이카드 뒷면" src="https://github.com/capstone2024-lyl/Back-End/assets/63050966/852cebb3-60c8-4213-80d5-7acfd6412d60">
<img width="166" alt="아이카드 색상" src="https://github.com/capstone2024-lyl/Back-End/assets/63050966/657b4b74-4a52-406b-b9bf-326858f35497">


### 진행 과정
- 각 검사를 통해 휴대폰의 사용내역을 분석
    - 채팅 : 별도 분석 모델을 제작하여 EC2에 배포 후 서비스에서 사용
    - 사진 분석 : 별도 분석 모델을 제작하여 온디바이스 방식으로 서비스에서 사용
- 각 검사 내용을 활용해 최종적으로 아이카드(I-Card, 자기소개카드)를 제작
- 아이카드의 앞면은 사용자의 분석 결과를 통한 칭호 확인과 대표적인 사용 기록을 확인 할 수 있고 뒷면을 통해 자세한 검사 결과 제공


### ERD Cloud

### Infrastructure
![캡스톤 인프라](https://github.com/capstone2024-lyl/Back-End/assets/63050966/4037c4a2-5cad-4633-8d1e-79bab2554eea)


## 참여인원

## 참여인원

| 역할 | 인원수 |
| --- | --- |
| Back-End | 1명 |
| Front-End | 1명 |
| Machine Learning | 1명 |


# 프로그램 실행 방법
- jdk 17이 설치되어 있어야 함.
- src/main/resources 하위에 src/main/resources/application.yml 파일이 필요


```
spring:

  datasource:
    url: jdbc:mysql://데이터베이스 주소 필요
    username: 사용자 이름
    password: 사용자 비밀번호
    driver-class-name: com.mysql.cj.jdbc.Driver

  sql:
    init:
      mode: never

  jwt:
    secret: jwt시크릿 키 설정
    expired-time: 3600000 # 1시간(1000 * 60 * 60)

  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 50MB

  openai:
    api:
      key: openai의 API 키 설정

  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        show_sql: true
        format_sql: true
        use_sql_comments: true
        hbm2ddl:
          auto: update
        default_batch_fetch_size: 1000

  flask:
    api:
      url: 모델에서 제공하는 API

cloud:
  aws:
    s3:
      bucketName: 사용자의 버킷 명
    credentials:
      accessKey: S3 액세스 키
      secretKey: S3 시크릿 키
    region:
      static: ap-northeast-2
    stack:
      auto: false

  default:
    profile:
      image:
        url: 기본 설정 프로필 이미지 URL

```

