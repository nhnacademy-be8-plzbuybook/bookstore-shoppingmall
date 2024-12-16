# 1. 사용할 베이스 이미지 (Temurin)
FROM eclipse-temurin:21-jre
# 2. 작업 디렉토리 설정
WORKDIR /app
# 3. JAR 파일 복사
COPY target/book-0.0.1-SNAPSHOT.jar /app/book.jar
# 4. 쇼핑몰 서버는 8090, 8091 포트를 사용함.
EXPOSE 8090 8091
# 5. 컨테이너 시작 시 실행할 명령어
CMD ["java", "-jar", "book.jar"]