package com.aliciatsai.parkinglot.controller;

import com.aliciatsai.parkinglot.model.ParkingLot;
import com.aliciatsai.parkinglot.model.Vehicle;
import com.aliciatsai.parkinglot.model.Vehicle.VehicleType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class ParkingLotController {

	private final ParkingLot parkingLot;

	public ParkingLotController() {
		this.parkingLot = new ParkingLot();
	}

	@GetMapping("/remainingSpaces")
	public int getRemainingSpaces() {
		return parkingLot.getTotalSpaceCount() - parkingLot.getTotalOccupiedSpaceCount();
	}

	@GetMapping("/isFull")
	public boolean isFull() {
		return getRemainingSpaces() == 0;
	}

	/**
	 * @param VehicleType motorcycle, car, or van
	 * @return amount of spaces occupied by vehicles of type VehicleType
	 */
	@GetMapping("/vehicle/count/{type}")
	public int getCount(@PathVariable VehicleType type) {
		return parkingLot.getOccupiedSpaceCount(type);
	}

	/**
	 * Parks vehicle in parking lot.
	 * @param VehicleType motorcycle, car, or van
	 * @param int plateNumber unique id of vehicle
	 * @return boolean if successfully parked
	 */
	@PutMapping("/vehicle/park/{type}/{plateNumber}")
	public boolean parkVehicle(@PathVariable VehicleType type, @PathVariable int plateNumber) {
		Vehicle vehicle = new Vehicle(plateNumber, type);
		return parkingLot.park(vehicle);
	}

	/**
	 * Removes vehicle from parking lot.
	 * @param int plate number of motorcycle, car, or van to remove
	 * @return void
	 * @throws IllegalArgumentException if vehicle not present in parking lot
	 */
	@DeleteMapping("/vehicle/remove/{plateNumber}")
	@ExceptionHandler(IllegalArgumentException.class)
	public void removeVehicle(@PathVariable int plateNumber) {
		parkingLot.remove(plateNumber);
		
	}
}
