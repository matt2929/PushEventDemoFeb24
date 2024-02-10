# PushEventDemoFeb24
Test pushing events in a micro-service arch


# build me

# do this once: 
brew install gradle

# run me locally
gradle wrapper
./gradlew build
java -jar  build/libs/PushEventDemoFeb24-1.0-SNAPSHOT.jar

Ran from root of package directory

```shell
docker build --no-cache  --progress plain -t demo-send-events . 
docker run -it --rm --name test1 demo-send-events
```





