# Concertrip_Server
#### 가장 먼저 받아보는 콘서트, 페스티벌 일정 구독 서비스

![앱아이콘](https://github.com/Concertrip/Concertrip_Server/blob/master/image/app_icon.png "앱아이콘")

* 2018 SOPT 23기해커톤
    *  프로젝트 기간 : 2018년 12월 22일 ~ 2019년 1월 12일
* API - (https://github.com/Concertrip/Concertrip_Server/wiki)
* 논리적 DB 모델링

![ERD](https://github.com/Concertrip/Concertrip_Server/blob/master/image/erd.JPG)


## 프로젝트 설명
* 어플의 소스코드가 하나의 프로젝트로 구성되어있으며 단일한 패티지로 배포되는 **모놀리틱 아키텍쳐**로 설계하였습니다.
* 보안적인 부분에서는 jwt토큰 기반 인증을 구현하였습니다. 
    * jwt토큰에 간단한 유저 정보를 넣어서 DB에 접근하는 횟수를 줄였습니다. 
    
* 데이터 베이스는 **몽고DB**와 **마리아 DB**를 사용하였습니다. 
    * 가수와 콘서트 그리고 장르 이렇게 3개의 컬렉션들의 데이터들은 쌓아놓고 삭제가 없을 뿐더러 READ가 빈번하기 때문에 NoSQL이 적합하다고 생각했습니다. 
    * 사용하기 쉽고, 리소스를 많이 요구하는 애플리케이션에 맞춰 확장할 수 있는 몽고 DB를 선택하였습니다.
 
*  지금은 작은 서비스이지만 운영 시스템의 중요도, 리스크 등을 감안하고 서버의 안정성을 생각해서 개발서버와 운영서버 두개를 운영하였습니다.
    * 개발 서버에서 거의 모든 환경이 운영과 같이 맞추어져 있어서 확인 후 운영쪽에 적용하였습니다.


## Architecture
![Architecture](https://github.com/Concertrip/Concertrip_Server/blob/master/image/Server_Architecture.png)


## 의존성
```xml
<dependencies>
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
        </dependency>
        <!-- AWS -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-aws</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!-- DB -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mariadb.jdbc</groupId>
            <artifactId>mariadb-java-client</artifactId>
        </dependency>
        <!-- LOMBOK -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>3.4.0</version>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.8.11</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.googlecode.json-simple</groupId>
            <artifactId>json-simple</artifactId>
            <version>1.1</version>
        </dependency>
    </dependencies>
```


## 시작하기
소스코드는 Intellij + Windows10 + JAVA 1.8.0 + SpringBoot 2.1.1 환경에서 작성되었습니다.



## 배포
* [AWS EC2](https://aws.amazon.com/ko/ec2/?sc_channel=PS&sc_campaign=acquisition_KR&sc_publisher=google&sc_medium=english_ec2_b&sc_content=ec2_e&sc_detail=aws%20ec2&sc_category=ec2&sc_segment=177228231544&sc_matchtype=e&sc_country=KR&s_kwcid=AL!4422!3!177228231544!e!!g!!aws%20ec2&ef_id=WkRozwAAAnO-lPWy:20180412120123:s) - 클라우드 환경 컴퓨팅 시스템
* [AWS RDS](https://aws.amazon.com/ko/rds/) - 클라우드 환경 데이터베이스 관리 시스템
* [AWS S3](https://aws.amazon.com/ko/s3/?sc_channel=PS&sc_campaign=acquisition_KR&sc_publisher=google&sc_medium=english_s3_b&sc_content=s3_e&sc_detail=aws%20s3&sc_category=s3&sc_segment=177211245240&sc_matchtype=e&sc_country=KR&s_kwcid=AL!4422!3!177211245240!e!!g!!aws%20s3&ef_id=WkRozwAAAnO-lPWy:20180412120059:s) - 클라우드 환경 데이터 저장소


## 사용된 도구
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Intellij IDEA](https://www.jetbrains.com/idea/) - 편집기
* [Mysql](https://www.mysql.com/) - DataBase
* [MongoDB](https://www.mongodb.com/) - DataBase


## 개발자
* 권순재 - [soonbee](https://github.com/soonbee)
* 김현진 - [hyunjkluz](https://github.com/hyunjkluz)
* 유현영 - [You-hyeonyeong](https://github.com/You-hyeonyeong)

[기여자 목록](https://github.com/Concertrip/Concertrip_Server/graphs/contributors)을 확인하여 이 프로젝트에 참가하신 분들을 보실 수 있습니다.

## Concertrip 의 다른 프로젝트
* [Android](https://github.com/Concertrip/Concertrip__Android)
* [iOS](https://github.com/Concertrip/Concertrip_iOS)

## wireframe
![wireframe](https://github.com/Concertrip/Concertrip_Server/blob/develop/image/workflow.JPG)