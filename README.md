## How to run

- Run axon server and mysql firstly

```
cd infra
docker-compose up
```

## Build common API & Run each service

- Build common API
```
cd common-api
mvn clean install
```

- Run each service
```
# new terminal
cd power plant
mvn clean spring-boot:run

# new terminal
cd metering billing
mvn clean spring-boot:run

```

- Run API gateway
```
cd gateway
mvn clean spring-boot:run
```

- Run frontend server
```
cd frontend
npm i
npm run serve

```

## Test By UI
Head to http://localhost:8088 with a web browser

## Test Rest APIs
- power plant
```
 http :8088/powerGenerations timestamp="timestamp" subscriberId="subscriberId" plantId="plantId" generatedAmount="generatedAmount" generatorType="generatorType" 
```
- metering billing
```
 http :8088/seps id="id" yearCode="yearCode" monthCode="monthCode" dayCode="dayCode" subscriberId="subscriberId" platId="platId" generationAmount="generationAmount" SEP="SEP" 
```

## Test RSocket APIs

- Download RSocket client
```
wget -O rsc.jar https://github.com/making/rsc/releases/download/0.4.2/rsc-0.4.2.jar
```
- Subscribe the stream
```
java -jar rsc.jar --stream  --route powerPlants.all ws://localhost:8088/rsocket/powerPlants

java -jar rsc.jar --stream  --route meteringBillings.all ws://localhost:8088/rsocket/meteringBillings

```
