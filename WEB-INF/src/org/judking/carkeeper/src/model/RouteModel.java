package org.judking.carkeeper.src.model;

import java.io.Serializable;
import java.util.Date;

public class RouteModel implements Serializable {
    private String route_id;
    private String vin;
    private Date start_time;
    private String duration;            //time unit: second
    private float miles;
    private float fuels;
    private int rapid_acc;
    private int rapid_dec;
    private float price;

    public RouteModel() {
    }

    public RouteModel(String vin, Date start_time, String duration) {
        this.vin = vin;
        this.start_time = start_time;
        this.duration = duration;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getMiles() {
        return miles;
    }

    public void setMiles(float miles) {
        this.miles = miles;
    }

    public float getFuels() {
        return fuels;
    }

    public void setFuels(float fuels) {
        this.fuels = fuels;
    }

    public int getRapid_acc() {
        return rapid_acc;
    }

    public void setRapid_acc(int rapid_acc) {
        this.rapid_acc = rapid_acc;
    }

    public int getRapid_dec() {
        return rapid_dec;
    }

    public void setRapid_dec(int rapid_dec) {
        this.rapid_dec = rapid_dec;
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
