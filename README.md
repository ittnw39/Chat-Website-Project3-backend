# SPATZ
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


## 기술 스택
<div>
    <img src="https://img.shields.io/badge/openjdk-000000?style=for-the-badge&logo=openjdk&logoColor=white">
    <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
    <img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<div>
    <img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white">
    <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
    <img src="https://img.shields.io/badge/nginx-009639?style=for-the-nginx&logo=nginx&logoColor=white">
    <img src="https://img.shields.io/badge/runner-FC6D26?style=for-the-badge&logoColor=white">
    <img src="https://img.shields.io/badge/websocket-FEEA3F?style=for-the-badge&logoColor=white">  </div>
</div>
<div>
    <img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
    <img src="https://img.shields.io/badge/amazonroute53-8C4FFF?style=for-the-badge&logo=amazonroute53&logoColor=white">
    <img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
    <img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">
</div>
<br>






