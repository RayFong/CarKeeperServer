package org.judking.carkeeper.src.model;

import java.io.Serializable;

public class VinDetailModel implements Serializable {
	private String vin_detail_id;
	private String vin;
	private String car_type;
	private String miles;
	

	@Override
	public String toString() {
		return "VinDetailModel [vin_detail_id=" + vin_detail_id + ", vin="
				+ vin + ", car_type=" + car_type + ", miles=" + miles + "]";
	}
	
	public String getVin_detail_id() {
		return vin_detail_id;
	}
	public void setVin_detail_id(String vin_detail_id) {
		this.vin_detail_id = vin_detail_id;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getCar_type() {
		return car_type;
	}
	public void setCar_type(String car_type) {
		this.car_type = car_type;
	}
	public String getMiles() {
		return miles;
	}
	public void setMiles(String miles) {
		this.miles = miles;
	}

}
