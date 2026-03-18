package parking;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import parking.VehicleType;

class Vehicle{
	
	String number;
	VehicleType type;
	public Vehicle(String number, VehicleType type) {
		this.number = number;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Vehicle Number:"+number+", Type:"+type;
	}
}

class ParkingSlot{
	int id;
	VehicleType type;
	boolean isOccupied;
	Vehicle vehicle;
	
	public ParkingSlot(int id,VehicleType type) {
		this.id=id;
		this.type=type;
		this.isOccupied=false;
	} 
	
	boolean canFitVehicle(Vehicle vehicle) {
		return (this.type==vehicle.type && !this.isOccupied);
	}
	
	void park(Vehicle vehicle) {
		this.isOccupied=true;
		this.vehicle=vehicle;
	}
	
	void unpark() {
		this.vehicle=null;
		this.isOccupied=false;
	}
}

class ParkingFloor{
	int floorNumber;
//	int bikeSlotsAvailable;
//	int carSlotsAvailable;
//	int busSlotsAvailable;
//	int truckSlotsAvailable;
	Map<VehicleType, Integer> slotsAvailable;
	List<ParkingSlot> slots;
	
	public ParkingFloor(int floorNumber,
			int bikeSlots,int carSlots,int busSlots,int truckSlots) {
		this.floorNumber=floorNumber;
//		this.bikeSlotsAvailable=bikeSlots;
//		this.carSlotsAvailable=carSlots;
//		this.busSlotsAvailable=busSlots;
//		this.truckSlotsAvailable=truckSlots;
		this.slotsAvailable=new HashMap<>();
		this.slotsAvailable.put(VehicleType.BIKE, bikeSlots);
		this.slotsAvailable.put(VehicleType.CAR, carSlots);
		this.slotsAvailable.put(VehicleType.BUS, busSlots);
		this.slotsAvailable.put(VehicleType.TRUCK, truckSlots);
		this.slots=new ArrayList<>();
		
		int id=1;
		
		for(int i=0;i<bikeSlots;i++) {
			slots.add(new ParkingSlot(id++, VehicleType.BIKE));
		}
		
		for(int i=0;i<carSlots;i++) {
			slots.add(new ParkingSlot(id++, VehicleType.CAR));
		}
		
		for(int i=0;i<busSlots;i++) {
			slots.add(new ParkingSlot(id++, VehicleType.BUS));
		}
		
		for(int i=0;i<truckSlots;i++) {
			slots.add(new ParkingSlot(id++, VehicleType.TRUCK));
		}
		
	}
	
	ParkingSlot assignParkingSlot(Vehicle v) {
		 for(ParkingSlot s:slots) {
			 if(s.canFitVehicle(v) && slotsAvailable.get(v.type)>0) {
				 s.park(v);
				 slotsAvailable.put(v.type,(slotsAvailable.get(v.type))-1);
//				 slotsAvailable.
				 return s;
			 }
		 }
		 return null;
	}
	
	boolean unpark(String vehicleNumber) {
		for (ParkingSlot parkingSlot : slots) {
			if(parkingSlot.isOccupied && parkingSlot.vehicle.number.equals(vehicleNumber)) {
				parkingSlot.unpark();
				slotsAvailable.put(parkingSlot.type,(slotsAvailable.get(parkingSlot.type))+1);
				return true;
			}
		}
		
		return false;
	}
	
	void printStatus() {
		for (ParkingSlot parkingSlot : slots) {
			System.out.println("Slot ID:"+parkingSlot.id+
								"\t Type:"+ parkingSlot.type +
								"\t Occupied:" + parkingSlot.isOccupied
								);
			
		}
		
		System.out.println("===Parkings available===");
		System.out.println("BIKES:" + slotsAvailable.get(VehicleType.BIKE));
		System.out.println("CARS:" + slotsAvailable.get(VehicleType.CAR));
		System.out.println("BUS:" + slotsAvailable.get(VehicleType.BUS));
		System.out.println("TRUCKS:" + slotsAvailable.get(VehicleType.TRUCK));
	}
}

public class ParkingLot {
	int floorCount;
	List<ParkingFloor> floors;
	
	public ParkingLot(int floorCount,int bikePerFloor,int carPerFloor,
						int busPerFloor,int truckPerFloor) {
		this.floorCount=floorCount;
		
		this.floors=new ArrayList<>();
		
		for(int i=0;i<floorCount;i++) {
			floors.add(new ParkingFloor(i+1, bikePerFloor, carPerFloor, busPerFloor, truckPerFloor));
		}
		
	}
	
	public boolean parkVehicle(Vehicle v) {
		for (ParkingFloor parkingFloor : floors) {
			ParkingSlot s= parkingFloor.assignParkingSlot(v);
			if(s!=null) {
				System.out.println("Vehicle:"+v+
						" parked at floor:"+parkingFloor.floorNumber+
						" at slot:"+s.id);
				return true;
			}
		}
		System.out.println("xxx No available slot for the Vehicle type:"+v.type);
		return false;
	}
	
	public boolean unparkVehicle(String vehicleNo) {
		for (ParkingFloor parkingFloor : floors) {
			if(parkingFloor.unpark(vehicleNo)) {
				System.out.println("Vehicle unparked from floor:"+parkingFloor.floorNumber);
				return true;
			}
		}
		
		System.out.println("xxx Vehicle NOT found xxx");
		return false;
	}
	
	public void getStatus() {
		System.out.println("===STATUS UPDATE OF THE PARKING LOT===");
		for (ParkingFloor parkingFloor : floors) {
			System.out.println("##Floor No:"+ parkingFloor.floorNumber);
			parkingFloor.printStatus();
		}
	}
	
	
	public static void main(String[] args) {
		ParkingLot lot=new ParkingLot(2, 4, 2, 1, 1);
		
		lot.parkVehicle(new Vehicle("KA-123-456",VehicleType.BIKE));
		lot.parkVehicle(new Vehicle("BR-434-456",VehicleType.CAR));
		lot.parkVehicle(new Vehicle("DL-325-456",VehicleType.BUS));
		lot.parkVehicle(new Vehicle("HR-726-456",VehicleType.TRUCK));
		
		lot.getStatus();
		
		lot.unparkVehicle("KA-123-456");
		lot.getStatus();
		
		lot.unparkVehicle("");
		lot.getStatus();

	}
	

}
/*
 * For scaling this 
 * use FACTORY design pattern for Creating Vehicles
 * use STRATEGY desgin pattern for Fee calculation
 * use SINGLETON desing pattern for central manager
 * */

/*
 * Optional Improvements 

	Add parking ticket system

	Add entry/exit time tracking

	Add pricing strategy

	Use enum with size mapping (BIKE < CAR < TRUCK)

	Optimize search (Map instead of linear scan)
 * 
 * */

/*
 * For ADVANCED LEVEL

 *  extend this into:

🔹 Multi-entry gates

🔹 Nearest slot allocation (priority queue)

🔹 EV charging slots

🔹 Real-time dashboard

🔹 DB schema + APIs

 * 
 * 
 * */
