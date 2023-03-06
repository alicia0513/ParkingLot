package com.aliciatsai.parkinglot.model;

import com.aliciatsai.parkinglot.exception.InvalidVehicleException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ParkingLotTest {

	@Test
	public void testPark_Sanity() {
		ParkingLot parkingLot = new ParkingLot();

		Vehicle motorcycle = new Vehicle(11, Vehicle.VehicleType.MOTORCYCLE);
		assertTrue(parkingLot.park(motorcycle));

		Vehicle van1 = new Vehicle(21, Vehicle.VehicleType.VAN);
		Vehicle van2 = new Vehicle(22, Vehicle.VehicleType.VAN);
		assertTrue(parkingLot.park(van1));
		assertTrue(parkingLot.park(van2));

		Vehicle car1 = new Vehicle(31, Vehicle.VehicleType.CAR);
		Vehicle car2 = new Vehicle(32, Vehicle.VehicleType.CAR);
		assertTrue(parkingLot.park(car1));
		assertFalse(parkingLot.park(car2));
	}

	@Test
	public void testRemove_HappyCase() {
		ParkingLot parkingLot = new ParkingLot();

		Vehicle motorcycle = new Vehicle(11, Vehicle.VehicleType.MOTORCYCLE);
		assertTrue(parkingLot.park(motorcycle));

		parkingLot.remove(11);
	}

	@Test
	public void testRemove_InvalidVehicle() {
		ParkingLot parkingLot = new ParkingLot();

		assertThrows(InvalidVehicleException.class, () -> parkingLot.remove(11));
	}
}
