# Ubuntu 기반의 이미지를 사용
FROM ubuntu:20.04

# 필수 패키지 업데이트 및 설치
RUN apt-get update && apt-get install -y \
    software-properties-common \
    && add-apt-repository ppa:openjdk-r/ppa \
    && apt-get update

# Java 17 설치
RUN apt-get install -y openjdk-17-jdk

# JAVA_HOME 환경 변수 설정
ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

# Java 설치 확인
RUN java -version

# 기본 명령어 설정 (필요에 따라 변경)
CMD ["bash"]