# Bank Transaction Application

### 목차
1. [개요](#1-개요)
2. [일정](#2-일정)
3. [문제해결 전략](#3-문제-해결-전략)
4. [테스트 방법](#4-테스트-방법)

### 1. 개요

해당 API 서비스는 은행 입출금 내역 조회를 목적으로 하며 **다음 기능**을 지원합니다.

* CSV 파일 프로세싱 후 데이터베이스에 저장
* 유저, 은행별 입출금 금액을 출력

서버 기술 스택은 다음과 같습니다.

* Spring Boot 2.6.3
* Java 11
* Spring Data JPA
* Netflix LoadBalancer
* Netflix Eureka
* Netflix Ribbon
* Junit
* Apache Commons CSV
* Log4j
* Resilience4j
* QueryDSL
* Mysql
* Python or VanillaJS(CSV 제출을 위한 테스트 클라이언트)

구체적인 서비스 아키텍처와 API 명세 등은 다음 [소개 문서](docs/1.Introduction.md)에서 확인 가능합니다. 

### 2. 일정

다음 [링크](docs/2.Schedule.md)에서 과제 수행 일정을 확인할 수 있습니다.

### 3. 문제해결 전략

다음 [링크](docs/3.ProblemSolving.md)에서 문제 해결 전략을 확인할 수 있습니다.

#### 대용량 처리 전략

1. Database Insert 처리 최적화: <u>Bulk Insert(Batch Insert)</u></br>
가장 Naive하게 여러 레코드의 Insert를 수행하면 N개의 레코드에 대해 N번의 SQL문을 실행하게 됩니다.
데이터베이스는 병목현상이 가장 빈번히 일어나는 지점이며, 비용이 크기에 하나의 SQL문에 여러개의 레코드를 묶어서 처리하는 Bulk Insert를 사용하였습니다.</br></br>
2. 확장성을 고려한 Loadbalancing 아키텍처 적용: <u>Spring Cloud Eureka</u>, <u>Spring Loadbalancer</u></br>
대용량 처리를 하려면 장기적으로 scale out이 가능한 아키텍처를 선택해야 합니다.
이에 따라 Eureka Server-Client 아키텍처를 적용해 여러대의 api 서버를 운용할 수 있도록 설계하였습니다.
Eureka가 서비스 디스커버리를 하고 loadbalancer가 client로 사용 가능한 서버에 호출하도록 했습니다.
이와 더불어 (static이 아닌) Dynamic 서비스 디스커버리이기에 운영이 용이합니다.</br></br>
3. Fault Tolerance 적용: <u>Resilience4j</u></br>
다루는 데이터의 특성으로 미루어 고객 Client뿐 아니라 다른 컴포넌트도 이용할 수 있다고 가정했습니다.
이에 따라 대용량 처리 시 에러가 발생할 수 있다 생각했고 따라서 Circuit Breaker를 적용해 장애 전파를 막도록 하였습니다.
초반에 Hystrix를 적용하였지만 Mono, Flux 처리에 있어 Resilience4j가 보다 편리하여 선택하였습니다. 설정은 Default 설정에 가깝습니다.</br>
4. WebClient를 통한 비동기처리: <u>WebClient</u></br>
LoadBalancer의 API 서버 호출에 있어 WebClient와 RestTemplate을 두고 고민한 결과 비동기처리를 하는 WebClient가 보다 유리하다고 판단하였습니다.

#### 동적 쿼리 전략
1. 동적 쿼리 생성: <u>QueryDSL</u></br>
과제 요구사항은 Nullable 파라미터 구현을 요구하는데, 이는 JPQL, SQL보다 QueryDSL이 적합하다고 판단하였습니다.
따라서 QueryDSL로 쿼리문을 동적으로 생성하여 사용하였으며 이는 다음 [클래스](./bank-transaction/src/main/java/com/BankTransactionApp/BankTransactionApp/web/banktransaction/repository/BankTransactionQueryRepository.java)에서 확인할 수 있습니다.

