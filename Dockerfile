FROM openjdk:13-jdk-alpine as builder

# Install Maven
RUN apk add --no-cache curl tar bash
ARG MAVEN_VERSION=3.6.3
ARG USER_HOME_DIR="/root"
RUN mkdir -p /usr/share/maven && \
curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar -xzC /usr/share/maven --strip-components=1 && \
ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"
# speed up Maven JVM a bit
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
ENTRYPOINT ["/usr/bin/mvn"]

# ----
# Install project dependencies and keep sources
# make source folder
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
# install maven dependency packages (keep in image)
COPY pom.xml /usr/src/app
RUN mvn -T 1C install -Dspring-boot.repackage.skip=true && rm -rf target
# copy other source files (keep in image)
COPY src /usr/src/app/src



RUN cd /usr/src/app && /usr/bin/mvn clean package

RUN echo "done!"


FROM openjdk:11-jre-slim
WORKDIR /spring

COPY ./tls.crt .

RUN  keytool -import -noprompt -trustcacerts -alias "lemtools" -file /spring/tls.crt -keystore /usr/local/openjdk-11/lib/security/cacerts -storepass changeit

COPY --from=builder /usr/src/app/target/*.jar ./app.jar

CMD ["java", "-jar", "./app.jar"]
ENTRYPOINT ["java", "-jar", "./app.jar"]

EXPOSE 8081 8443 443