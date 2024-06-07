# 2024-1학기 중앙대학교 소프트웨어학부 캡스톤 디자인 Back-End
## 아이카드(I-Card, 나를 알고 소개하다)


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
