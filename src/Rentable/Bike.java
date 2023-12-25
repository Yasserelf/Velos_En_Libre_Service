package Rentable;

import java.util.*;

import equipements.Equipment;

public abstract class Bike {
	
	protected List<Equipment> equipments;
	
	public Bike() {
		this.equipments = new ArrayList<>();
	}

	public boolean isEquiped(Equipment e) {
		return this.equipments.contains(e);
	}


	public void addEquipment(Equipment obj) {
		this.equipments.add(obj);
	}
	
	public List<Equipment> getEquipmentList() {
		return this.equipments;
	}
	
	public void setEquipmentList(List<Equipment> l) {
		this.equipments = l ;
	}
	
		
	
}
