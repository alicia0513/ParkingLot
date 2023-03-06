package com.aliciatsai.parkinglot.model;

import com.aliciatsai.parkinglot.exception.InvalidVehicleException;
import lombok.NonNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.CollectionUtils;

public final class ParkingLot {
	private static final int CAR_SPACES_COUNT = 4;
	private static final int MOTORCYCLE_SPACES_COUNT = 1;
	private static final int VAN_SPACES_COUNT = 1;

	// move to DB storage for stateless/scalability
	private final ParkingSpace[] carSpaces;
	private final ParkingSpace[] motorcycleSpaces;
	private final ParkingSpace[] vanSpaces;

	private final Map<Integer, List<ParkingSpace>> vehicleIdToSpace; // map from parked vehicle plate number to space in which it's parked
	private final Map<Vehicle.VehicleType, Integer> occupiedSpacesByVehicleType; // map from vehicle type to amount of vehicles of type parked in parking lot

	public ParkingLot() {
		carSpaces = new ParkingSpace[CAR_SPACES_COUNT];
		for (int i = 0; i < carSpaces.length; i++) {
			carSpaces[i] = new ParkingSpace();
		}
		motorcycleSpaces = new ParkingSpace[MOTORCYCLE_SPACES_COUNT];
		for (int i = 0; i < motorcycleSpaces.length; i++) {
			motorcycleSpaces[i] = new ParkingSpace();
		}
		vanSpaces = new ParkingSpace[VAN_SPACES_COUNT];
		for (int i = 0; i < vanSpaces.length; i++) {
			vanSpaces[i] = new ParkingSpace();
		}
		vehicleIdToSpace = new HashMap<>();
		occupiedSpacesByVehicleType = new HashMap<>();
		occupiedSpacesByVehicleType.put(Vehicle.VehicleType.MOTORCYCLE, 0);
		occupiedSpacesByVehicleType.put(Vehicle.VehicleType.CAR, 0);
		occupiedSpacesByVehicleType.put(Vehicle.VehicleType.VAN, 0);
	}

	// @Override
	// public String toString() {
	// 	StringBuilder string = new StringBuilder();
	// 	string.append("Car Spaces: \n");
	// 	for (ParkingSpace space : carSpaces) {
	// 		string.append(space.getParkedVehicle());
	// 		string.append("\n");
	// 	}
	// 	string.append("Motor Spaces: \n");
	// 	for (ParkingSpace space : motorcycleSpaces) {
	// 		string.append(space.getParkedVehicle());
	// 		string.append("\n");
	// 	}
	// 	string.append("Van Spaces: \n");
	// 	for (ParkingSpace space : vanSpaces) {
	// 		string.append(space.getParkedVehicle());
	// 		string.append("\n");
	// 	}
	// 	return string.toString();
	// }

	/**
	 * @return int amount of total spaces in parking lot (occupied + unoccupied)
	 */
	public int getTotalSpaceCount() {
		return CAR_SPACES_COUNT + MOTORCYCLE_SPACES_COUNT + VAN_SPACES_COUNT;
	}

	/**
	 * @return int amount of occupied spaces
	 */
	public int getTotalOccupiedSpaceCount() {
		int total = 0;
		for (Vehicle.VehicleType type : occupiedSpacesByVehicleType.keySet()) {
			total += occupiedSpacesByVehicleType.get(type);
		}
		return total;
	}

	/**
	 * @param Vehicle.VehicleType motorcycle, car, or van
	 * @return int amount of occupied spaces by vehicles of type VehicleType
	 */
	public int getOccupiedSpaceCount(@NonNull Vehicle.VehicleType type) {
		return occupiedSpacesByVehicleType.getOrDefault(type, 0);
	}

	/**
	 * Parks vehicle in space and updates vehicleIdToSpace and occupiedSpacesByVehicleType maps
	 * @param ParkingSpace space in which to park vehicle
	 * @param Vehicle vehicle to park
	 * @return void
	 */
	private void parkVehicleAndUpdateData(@NonNull ParkingSpace space, @NonNull Vehicle vehicle) {
		space.setParkedVehicle(vehicle);

		List<ParkingSpace> spaces = vehicleIdToSpace.getOrDefault(vehicle.getPlateNumber(), new ArrayList<>());
		spaces.add(space);
		vehicleIdToSpace.put(vehicle.getPlateNumber(), spaces);

		occupiedSpacesByVehicleType.merge(vehicle.getType(), 1, (a,b) -> a+b);
	}

	/**
	 * Parks vehicle in parking lot.
	 * Motorcycle will park in motorcycle then car then van spot depending on availability.
	 * Car will park in car then van spot depending on availability.
	 * Van will park in van spot then 3 consecutive car spots depending on availability.
	 * @param Vehicle motorcycle, car, or van to park
	 * @return boolean if successfully parked
	 */
	public boolean park(@NonNull Vehicle vehicle) {
		switch (vehicle.getType()) {
			case MOTORCYCLE:
				for (ParkingSpace space : motorcycleSpaces) {
					if (!space.isOccupied()) {
						parkVehicleAndUpdateData(space, vehicle);
						return true;
					}
				}
			case CAR: // MOTORCYCLE fallthrough
				for (ParkingSpace space : carSpaces) {
					if (!space.isOccupied()) {
						parkVehicleAndUpdateData(space, vehicle);
						return true;
					}
				}
			case VAN: // MOTORCYCLE/CAR fallthrough
				for (ParkingSpace space : vanSpaces) {
					if (!space.isOccupied()) {
						parkVehicleAndUpdateData(space, vehicle);
						return true;
					}
				}
				if (vehicle.getType() == Vehicle.VehicleType.VAN) { // must specify VAN due to fallthrough
					int consecutiveCarSpaceCount = 0;
					for (int i = 0; i < carSpaces.length; i++) {
						if (!carSpaces[i].isOccupied()) {
							consecutiveCarSpaceCount++;
							if (consecutiveCarSpaceCount == 3) {
								while (consecutiveCarSpaceCount > 0) {
									parkVehicleAndUpdateData(carSpaces[i], vehicle);
									i--;
									consecutiveCarSpaceCount--;
								}
								return true;
							}
						} else {
							consecutiveCarSpaceCount = 0;
						}
					}
				}
		}
		return false;
	}

	/**
	 * Removes vehicle from parking lot.
	 * @param int plate number of motorcycle, car, or van to remove
	 * @return void
	 * @throws InvalidVehicleException if vehicle not present in parking lot
	 */
	public void remove(int plateNumber) throws InvalidVehicleException {
		List<ParkingSpace> spaces = vehicleIdToSpace.remove(plateNumber);
		if (CollectionUtils.isEmpty(spaces)) {
			throw new InvalidVehicleException(plateNumber + " not found in parking lot");
		}
		for (ParkingSpace space : spaces) {
			Vehicle vehicle = space.getParkedVehicle();
			space.setParkedVehicle(null);
			occupiedSpacesByVehicleType.merge(vehicle.getType(), 1, (a,b) -> a-b);
		}
	}
}