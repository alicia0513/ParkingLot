package com.aliciatsai.parkinglot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ParkingSpace {
	@Getter private boolean isOccupied;
	@Getter private Vehicle parkedVehicle;

	public void setParkedVehicle(Vehicle vehicle) {
		parkedVehicle = vehicle;
		isOccupied = parkedVehicle != null;
	}
}