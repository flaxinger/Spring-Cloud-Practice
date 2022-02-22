# Bank Transaction Application

### 목차
1. [개요](#1-개요)
2. [일정](#2-일정)
3. [문제해결 전략](#3-문제-해결-전략)
4. [테스트 방법](#4-테스트-방법)

### 1. 개요

해당 API 서비스는 은행 입출금 내역 조회를 목적으로 하며 **다음 기능**을 지원한다.

* CSV 파일 프로세싱 후 데이터베이스에 저장
* 유저, 은행별 입출금 금액을 출력

서버 기술 스택은 다음과 같다.

* Spring Boot 2.6.3
* Java 11
* Spring Data JPA
* Spring Cloud(Circuit Breaker)
* Mysql
* Python or VanillaJS(CSV 제출을 위한 테스트 클라이언트)

구체적인 서비스 아키텍처와 API 명세 등은 다음 문서에서 확인 가능합니다. [소개 문서](docs/1.Introduction.md)

### 2. 일정

다음 [링크](docs/2.Schedule.md)에서 과제 수행 일정을 확인할 수 있습니다.

### 3. 문제해결 전략

다음 [링크](docs/3.ProblemSolving.md)에서 문제 해결 전략을 확인할 수 있습니다.

### 4. 테스트 방법

다음 [링크](docs/4.LocalTesting.md)에서 테스트 방법을 확인할 수 있습니다.