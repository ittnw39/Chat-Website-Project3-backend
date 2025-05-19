# 🚀 Spatz: 실시간 채팅 웹 애플리케이션 백엔드 💬
![spatz-logo](/uploads/75c80f561531b5ab9b89070626774286/spatz-logo.png)
<br>


## 팀 소개
- 엘리스 Cloud 트랙 3기 6팀
- 팀원: 강대희, 권재우, 안승우, 우연정, 정동희, 조혜연
- 노션: https://www.notion.so/elice-track/6-564ec5d05b6b4045b5f6b322c61ca903
<br>

## 프로젝트 소개
- 기간: 2024.07.22 ~ 2024.08.17
- 주제: 실시간 채팅, 음성 공유 기능을 제공하는 서비스 개발
- 특징: 향후 **MSA 구조로의 전환**을 염두에 두고, 현재 프로젝트는 `user`, `chat`, `file` **세 도메인을 독립적으로 수직 분리**하여 개발하였습니다.

Discord와 유사한 실시간 음성/영상 통화 및 채팅 기능을 제공하는 웹 애플리케이션 "Spatz"의 백엔드 프로젝트입니다.
이 프로젝트는 특히 AWS S3를 활용하여 채팅 중 파일을 안전하고 효율적으로 첨부하고, 전송, 다운로드하며, 채팅 메시지 내에 해당 파일 정보를 저장하여 공유하는 기능을 핵심적으로 구현했습니다.

## ✨ 주요 기능

*   **실시간 텍스트 채팅**: WebSocket을 이용한 실시간 양방향 텍스트 메시징
*   **AWS S3 기반 파일 공유**:
    **(참고: 채팅 중 파일 첨부, 전송, 다운로드 및 S3 연동을 포함한 모든 파일 관련 기능은 본 프로젝트 참여자인 제가 담당하여 개발했습니다.)**
    *   **파일 업로드**: 이미지, 문서 등 다양한 파일을 S3에 안전하게 업로드합니다.
    *   **파일 다운로드**: S3에 저장된 파일을 사용자가 다운로드할 수 있습니다.
    *   **채팅 내 파일 표시/저장**: 채팅 메시지에 파일 정보를 포함하여, 채팅창에서 바로 확인하거나 접근할 수 있도록 합니다. (예: S3 Presigned URL 사용)
    *   **파일 삭제**: S3에 업로드된 파일 삭제 기능을 제공합니다.
*   **사용자 인증 및 인가**: Spring Security와 JWT를 사용한 안전한 사용자 인증 및 API 접근 제어
*   **소셜 로그인**: OAuth 2.0을 이용한 간편 로그인 기능
*   **음성/영상 통화**: OpenVidu를 활용한 실시간 음성 및 영상 통화 기능
<br>

## 배포 주소
- <http://elice-build.s3-website.ap-northeast-2.amazonaws.com/>
<br>

## 사용 방법
1. 클론: <https://kdt-gitlab.elice.io/cloud_track/class_03/web_project3/team06/multi-module-project.git>
2. 의존성 설치: gradle 실행
3. 실행: 실행 버튼 클릭
4. 접속: <http://localhost:8080/>
<br>

## 파일 구조
```bash
💻 multi-module-project
├─ src
│  └─ main
│     └─ java/com/elice/spatz
│        ├─ config
│        ├─ constans
│        ├─ domain
│        │  ├─ chat
│        │  ├─ file
│        │  ├─ reaction
│        │  ├─ server
│        │  ├─ serverUser
│        │  ├─ user
│        │  └─ userFeature
│        ├─ entity/baseEntity
│        ├─ exception
│        ├─ filter
│        └─ SpatzApplication.java
├─ Dockerfile
└─ build.gradle
```
<br>


## 🛠️ 기술 스택

*   **언어**: ![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java&logoColor=white)
*   **프레임워크**: ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
*   **데이터베이스**:
    *   ![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
    *   ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
    *   ![H2 Database](https://img.shields.io/badge/H2_Database-464646?style=for-the-badge&logo=h2&logoColor=white) (개발용)
    *   ![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
*   **클라우드 서비스 (AWS)**:
    *   ![AWS S3](https://img.shields.io/badge/AWS_S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white) (파일 저장소)
    *   ![Spring Cloud AWS](https://img.shields.io/badge/Spring_Cloud_AWS-F8991D?style=for-the-badge&logo=amazonaws&logoColor=white)
*   **실시간 통신**:
    *   ![Spring WebSocket](https://img.shields.io/badge/Spring_WebSocket-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
    *   ![OpenVidu](https://img.shields.io/badge/OpenVidu-E2001A?style=for-the-badge&logo=openvidu&logoColor=white) (음성/영상 통화)
*   **보안**:
    *   ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
    *   ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white) (JSON Web Token)
    *   ![OAuth 2.0](https://img.shields.io/badge/OAuth_2.0-33CCFF?style=for-the-badge&logo=oauth&logoColor=white)
*   **개발 도구 및 라이브러리**:
    *   ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
    *   ![Lombok](https://img.shields.io/badge/Lombok-DC382D?style=for-the-badge&logo=projectlombok&logoColor=white)
    *   ![MapStruct](https://img.shields.io/badge/MapStruct-FF69B4?style=for-the-badge&logo=mapstruct&logoColor=white)
    *   ![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

## 📁 프로젝트 구조 (주요 경로)

\'\'\'
.
├── src/main/java/com/elice/spatz/
│   ├── SpatzApplication.java  # Spring Boot 메인 애플리케이션
│   ├── config/                # 애플리케이션 설정 (보안, AWS, WebSocket 등)
│   │   └── SpatzApplication.java # Spring Boot 메인 애플리케이션
│   ├── domain/
│   │   ├── chat/              # 채팅 관련 (Controller, Service, Entity, DTO 등)
│   │   │   ├── entity/
│   │   │   │   └── ChatMessage.java # 채팅 메시지 엔티티 (또는 DTO)
│   │   │   └── service/
│   │   │       └── ChatService.java # 채팅 메시지 처리 서비스
│   │   ├── file/              # 파일 처리 관련 (Controller, Service, DTO 등)
│   │   │   ├── service/
│   │   │   │   └── FileService.java # AWS S3 파일 업로드/다운로드/삭제 서비스
│   │   ├── user/              # 사용자 관련
│   │   └── ...                # 기타 도메인
│   ├── exception/             # 예외 처리
│   └── ...
├── build.gradle               # 프로젝트 빌드 및 의존성 관리
├── Dockerfile                 # Docker 이미지 빌드 설정
└── ...
\'\'\'

## 🚀 시작하기

### 사전 준비물

*   Java 17
*   Gradle
*   MySQL (또는 설정된 다른 데이터베이스)
*   AWS 계정 및 S3 버킷 설정 (자격 증명은 `application.yml` 또는 환경 변수로 설정)

### 실행 방법

1.  **프로젝트 클론**:
    \'\'\'bash
    git clone https://github.com/ittnw39/Chat-Website-Project3-backend.git
    cd <프로젝트 디렉토리>
    \'\'\'

2.  **애플리케이션 설정**:
    `src/main/resources/application.yml` (또는 `.properties`) 파일에 데이터베이스, AWS S3, JWT 시크릿 키 등 필요한 설정을 구성합니다.

3.  **빌드 및 실행**:
    \'\'\'bash
    ./gradlew bootRun
    \'\'\'
    또는 IDE를 통해 `SpatzApplication.java` 파일을 실행합니다.

## 📝 TODO (추가 예정 기능)

*   **`ChatMessage` 엔티티/DTO 개선**: 파일 첨부 정보(S3 파일 키, 원본 파일명 등)를 명시적 필드로 추가하여 메시지와 파일 간의 관계 명확화 및 로직 안정성/가독성 향상.
*   **`FileService` 리팩토링**: `getEncodedFilename` 메서드의 브라우저별 파일명 인코딩 로직을 별도 유틸리티로 분리 또는 표준화된 방식으로 개선하여 가독성 및 유지보수성 증진.
*   **`SecurityConfig` 설정 분리**: CORS, JWT, URL 권한 등 다양한 보안 설정을 각 관심사에 따라 별도 클래스/메서드로 분리하여 관리 용이성 향상.
*   **전역 예외 처리 및 응답 표준화**: `@ControllerAdvice` 및 커스텀 예외를 활용하여 API 응답 형식 표준화 및 일관된 에러 메시지 제공.
*   (기타 필요한 리팩토링 및 기능 개선 사항 추가하기 . . .)






