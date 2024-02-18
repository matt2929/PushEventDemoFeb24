# using multistage docker build
# ref: https://docs.docker.com/develop/develop-images/multistage-build/
    
# temp container to build using gradle
FROM gradle:latest AS TEMP_BUILD_IMAGE
ENV FOO=$(pwd)
RUN echo $FOO
WORKDIR /usr/app/
COPY . .
RUN ls /usr/app/
RUN gradle build
    
# actual container
FROM openjdk:latest
ENV ARTIFACT_NAME=PushEventDemoFeb24-1.0-SNAPSHOT.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME .
RUN groupadd -r -g 1000 user && useradd -r -g user -u 1000 user
RUN chown -R user:user /usr/app/
USER user
ENTRYPOINT exec java -jar $APP_HOME/build/libs/${ARTIFACT_NAME} PUSH