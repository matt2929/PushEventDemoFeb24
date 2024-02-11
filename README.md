# PushEventDemoFeb24
test pulling events from a microservice architecture

# how to run locally
gradle wrapper
./gradlew build
java -jar build/libs/PushEventDemoFeb24-1.0-SNAPSHOT.jar

# run in Docker
docker build  --progress plain -t demo-send-events .
docker run -it --rm --name test1 demo-send-events 