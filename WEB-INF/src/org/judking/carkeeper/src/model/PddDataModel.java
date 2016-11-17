package org.judking.carkeeper.src.model;

import java.io.Serializable;
import java.util.Date;

public class PddDataModel implements Serializable {
    private String pdd_id;
    private String route_id;
    private Date date;
    private String cmd;
    private String ripeData;

    @Override
    public String toString() {
        return "PddDataModel [pdd_id=" + pdd_id + ", route_id=" + route_id
                + ", date=" + date + ", cmd=" + cmd + ", ripeData=" + ripeData
                + "]";
    }

    public String getPdd_id() {
        return pdd_id;
    }

    public void setPdd_id(String pdd_id) {
        this.pdd_id = pdd_id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getRipeData() {
        return ripeData;
    }

    public void setRipeData(String ripeData) {
        this.ripeData = ripeData;
    }

}
