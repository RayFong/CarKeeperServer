package org.judking.carkeeper.src.model;

import java.io.Serializable;
import java.util.Date;

public class RouteModel implements Serializable {
	private String route_id;
	private String vin;
	private Date start_time;
	private String duration;			//time unit: second
	
	public RouteModel() {
	}
	
	public RouteModel(String vin, Date start_time, String duration) {
		this.vin = vin;
		this.start_time = start_time;
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "RouteModel [route_id=" + route_id + ", vin=" + vin
				+ ", start_time=" + start_time + ", duration=" + duration + "]";
	}
	
	public String getRoute_id() {
		return route_id;
	}
	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Date getStart_time() {
		return start_time;
	}
	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}

	
	
}
