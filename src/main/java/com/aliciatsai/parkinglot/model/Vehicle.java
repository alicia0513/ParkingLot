package com.aliciatsai.parkinglot.model;

import lombok.Getter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class Vehicle {
	public enum VehicleType {
		CAR,
		MOTORCYCLE,
		VAN
	}

	@Getter private final int plateNumber; // unique id of vehicle
	@Getter private final VehicleType type;

}