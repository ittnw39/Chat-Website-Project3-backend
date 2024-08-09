# 베이스 이미지로 ubuntu를 사용합니다.
FROM ubuntu:20.04

# 불필요한 경고 메시지를 방지하기 위해 환경변수를 설정합니다.
ENV DEBIAN_FRONTEND=noninteractive

# 필요한 패키지를 설치하고 OpenJDK 17을 설치합니다.
RUN apt-get update && \
    apt-get install -y software-properties-common && \
    add-apt-repository ppa:openjdk-r/ppa && \
    apt-get update && \
    apt-get install -y openjdk-17-jdk

# Java의 환경변수를 설정합니다.
ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

# 작업 디렉토리를 설정
WORKDIR /app

# 파일 복사
COPY /build/libs/spatz-0.0.1-SNAPSHOT.jar spatz.jar

# 커맨드 실행
CMD ["java", "-jar", "spatz.jar"]