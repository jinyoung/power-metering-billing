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
 http :8081/powerGenerations id="230317-nb" subscriberId="subscriberId" plantId="plantId"  

 http PUT :8081/powerGenerations/230317-nb/generate generatedAmount=100
 http PUT :8081/powerGenerations/230317-nb/generate generatedAmount=100

 http :8081/powerGenerations/230317-nb   # generated amount must be 200
 
```
- metering billing
```
 http :8082/meterings
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
