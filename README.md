# PushEventDemoFeb24

This directory consists of a docker compose application.

1) A locally built Java push service that pushes messages to a rabbit mq service
2) A locally built Java pull service that polls messages from a rabbit mq service
3) A rabbit mq image deployed for 1. and 2.

1 and 2 are built in this directory via gradle and have their respective Dockerfiles built and provided to compose.

# how to run locally

## The Push Application

### Run Locally

_Note_: I won't connect because I expect a bridge Compose network to resolve RabbitMQ hostname.

```bash
gradle wrapper
./gradlew build
java -jar build/libs/PushEventDemoFeb24-1.0-SNAPSHOT.jar PUSH
```

### run in Docker

```bash
docker build --build-arg PROFILE="PUSH"   -f Dockerfiles/queue-application/Dockerfile  --no-cache --progress plain -t demo-send-events .
docker run -it --rm --name test1 demo-send-events  
```

## The Pull Application

### Run Locally

_Note_: I won't connect because I expect a bridge Compose network to resolve RabbitMQ hostname.

```bash
gradle wrapper
./gradlew build
java -jar build/libs/PushEventDemoFeb24-1.0-SNAPSHOT.jar PULL
```

### run in Docker

```bash
docker build --build-arg PROFILE="PULL"   -f Dockerfiles/queue-application/Dockerfile  --no-cache --progress plain -t demo-send-events .
docker run -it --rm --name test1 demo-pull-events 
```

### Run it in Docker Compose

```bash
docker rm -f $(docker ps -a -q)
docker volume rm $(docker volume ls -q)
 docker compose up --force-recreate --build
```

```bash
curl -d "@data.json" -X POST  http://localhost:8080/createEmployee -H 'Content-Type: application/json'
```

```json
{
  "uuid": "6eccfccf-05aa-414c-8857-f7c1889722a9",
  "itemType": "foo",
  "monetaryAmount": 1.0
}
```

mongodb:
http://localhost:8081/

