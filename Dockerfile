# Tomcat 기반의 공식 이미지 사용
FROM tomcat:10.1.31-jdk21

# 기존 webapps 폴더 정리 (선택 사항)
RUN rm -rf /usr/local/tomcat/webapps/*

# WAR 파일 복사 및 배포
COPY target/project-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/project.war

# Tomcat 서버 실행
CMD ["catalina.sh", "run"]
