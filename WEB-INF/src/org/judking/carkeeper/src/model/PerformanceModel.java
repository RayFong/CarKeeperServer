package org.judking.carkeeper.src.model;

/**
 * Created by leilf on 2017/4/28.
 */
public class PerformanceModel {
    private String route_id;
    private double oxygen;
    private double inject;
    private double catalyst;

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public double getOxygen() {
        return oxygen;
    }

    public void setOxygen(double oxygen) {
        this.oxygen = oxygen;
    }

    public double getInject() {
        return inject;
    }

    public void setInject(double inject) {
        this.inject = inject;
    }

    public double getCatalyst() {
        return catalyst;
    }

    public void setCatalyst(double catalyst) {
        this.catalyst = catalyst;
    }
}
