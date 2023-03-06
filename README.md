### Requirments
- Java 17
- gradle

### Development
- Run `./gradlew bootRun`
- Run `./gradew test` for unit tests

### APIs
`GET /remainingSpaces`
`GET /isFull`
`GET /vehicle/count/{CAR/MOTORCYCLE/VAN}`
`PUT /vehicle/park/{CAR/MOTORCYCLE/VAN}/{plateNumber}`
`DELETE /vehicle/remove/{plateNumber}`

### Manual Testing Commands
Test init
`curl -X GET localhost:8080/remainingSpaces` //expect 6
`curl -X GET localhost:8080/isFull` //expect false
`curl -X GET localhost:8080/vehicle/count/CAR`//expect 0
`curl -X GET localhost:8080/vehicle/count/MOTORCYCLE`//expect 0
`curl -X GET localhost:8080/vehicle/count/VAN`//expect 0

Test parking motorcycle, overflow to car size spaces
`curl -X PUT localhost:8080/vehicle/park/MOTORCYCLE/11` //expect true
`curl -X PUT localhost:8080/vehicle/park/MOTORCYCLE/12` //expect true
`curl -X GET localhost:8080/remainingSpaces` //expect 4
`curl -X GET localhost:8080/isFull` //expect false
`curl -X GET localhost:8080/vehicle/count/CAR`//expect 0
`curl -X GET localhost:8080/vehicle/count/MOTORCYCLE`//expect 2
`curl -X GET localhost:8080/vehicle/count/VAN`//expect 0

Test delete
`curl -X DELETE localhost:8080/vehicle/remove/12`
`curl -X GET localhost:8080/remainingSpaces` //expect 5
`curl -X GET localhost:8080/isFull` //expect false
`curl -X GET localhost:8080/vehicle/count/CAR`//expect 0
`curl -X GET localhost:8080/vehicle/count/MOTORCYCLE`//expect 1
`curl -X GET localhost:8080/vehicle/count/VAN`//expect 0

Test parking cars, overflow to van size spaces
`curl -X PUT localhost:8080/vehicle/park/CAR/21` //expect true
`curl -X PUT localhost:8080/vehicle/park/CAR/22` //expect true
`curl -X PUT localhost:8080/vehicle/park/CAR/23` //expect true
`curl -X PUT localhost:8080/vehicle/park/CAR/24` //expect true
`curl -X PUT localhost:8080/vehicle/park/CAR/25` //expect true
`curl -X GET localhost:8080/remainingSpaces` //expect 0
`curl -X GET localhost:8080/isFull` //expect true
`curl -X GET localhost:8080/vehicle/count/CAR`//expect 5
`curl -X GET localhost:8080/vehicle/count/MOTORCYCLE`//expect 1
`curl -X GET localhost:8080/vehicle/count/VAN`//expect 0

Test delete
`curl -X DELETE localhost:8080/vehicle/remove/22` //expect true
`curl -X DELETE localhost:8080/vehicle/remove/23` //expect true
`curl -X DELETE localhost:8080/vehicle/remove/24` //expect true
`curl -X GET localhost:8080/remainingSpaces` //expect 3
`curl -X GET localhost:8080/isFull` //expect false
`curl -X GET localhost:8080/vehicle/count/CAR`//expect 2
`curl -X GET localhost:8080/vehicle/count/MOTORCYCLE`//expect 1
`curl -X GET localhost:8080/vehicle/count/VAN`//expect 0

Test parking van in 3 car spaces
`curl -X PUT localhost:8080/vehicle/park/VAN/31` //expect true
`curl -X GET localhost:8080/remainingSpaces` //expect 0
`curl -X GET localhost:8080/isFull` //expect true
`curl -X GET localhost:8080/vehicle/count/CAR`//expect 2
`curl -X GET localhost:8080/vehicle/count/MOTORCYCLE`//expect 1
`curl -X GET localhost:8080/vehicle/count/VAN`//expect 3

Test parking van in full lot
`curl -X PUT localhost:8080/vehicle/park/VAN/32` //expect false
`curl -X GET localhost:8080/remainingSpaces` //expect 0
`curl -X GET localhost:8080/isFull` //expect true
`curl -X GET localhost:8080/vehicle/count/CAR`//expect 2
`curl -X GET localhost:8080/vehicle/count/MOTORCYCLE`//expect 1
`curl -X GET localhost:8080/vehicle/count/VAN`//expect 3

Test parking van in van space
`curl -X DELETE localhost:8080/vehicle/remove/25` //expect true
`curl -X PUT localhost:8080/vehicle/park/VAN/32` //expect true
`curl -X GET localhost:8080/remainingSpaces` //expect 0
`curl -X GET localhost:8080/isFull` //expect true
`curl -X GET localhost:8080/vehicle/count/CAR`//expect 1
`curl -X GET localhost:8080/vehicle/count/MOTORCYCLE`//expect 1
`curl -X GET localhost:8080/vehicle/count/VAN`//expect 4

Test delete vehicle that doesn't exist
`curl -X DELETE localhost:8080/vehicle/remove/33` //expect exception
