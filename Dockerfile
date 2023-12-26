FROM openjdk:11-jre-slim
WORKDIR /spring

COPY ./tls.crt .

RUN  keytool -import -noprompt -trustcacerts -alias "lemtools" -file /spring/tls.crt -keystore /usr/local/openjdk-11/lib/security/cacerts -storepass changeit

COPY target/*.jar ./app.jar

CMD ["java", "-jar", "./app.jar"]
ENTRYPOINT ["java", "-jar", "./app.jar"]

EXPOSE 8081 8443 443