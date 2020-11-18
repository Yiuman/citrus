FROM java:8-jre
LABEL author="yiuman"

COPY citrus-main-0.0.1.jar /citrus.jar
COPY application-docker.yml /config/application.yml

ENV MYSQL_URL jdbc:mysql://mysql-local:3306/citrus?zeroDateTimeBehavior=convertToNull&characterEncoding=UTF-8
ENV MYSQL_USERNAME yourusername
ENV MYSQL_PASSWORD yourpassword
ENV PARAMS ""

EXPOSE 80

ENTRYPOINT java -jar /citrus.jar $PARAMS