FROM gradle:8.8.0-jdk17 as builder
WORKDIR /build

# Gradle 파일 복사 및 빌드 의존성 다운로드
COPY build.gradle settings.gradle /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

# 애플리케이션 빌드
COPY . /build
RUN gradle build -x test --parallel

# 애플리케이션 실행 환경 설정 (JDK 17 사용)
FROM openjdk:17.0-slim
WORKDIR /app

# 빌더 이미지에서 JAR 파일 복사
COPY --from=builder /build/build/libs/*-SNAPSHOT.jar ./app.jar

EXPOSE 8080

# root 대신 nobody 권한으로 실행
USER nobody
ENTRYPOINT [                                                \
    "java",                                                 \
    "-jar",                                                 \
    "-Djava.security.egd=file:/dev/./urandom",              \
    "-Dsun.net.inetaddr.ttl=0",                             \
    "app.jar"                                               \
]