package org.judking.carkeeper.src.bean;

import org.judking.carkeeper.src.model.PddDataModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leilf on 2017/3/7.
 */
public class RouteBean {
    private String user;
    private String token;
    private Date startDate;
    private String vin;
    private long duration;
    private Map<String, List<PddDataModel>> commands;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Map<String, List<PddDataModel>> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, List<PddDataModel>> commands) {
        this.commands = commands;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Override
    public String toString() {
        return "RouteBean [user=" + user + ", token=" + token + ", startDate=" + startDate + ", vin=" + vin +
                ", duration=" + duration + ", commands=" + commands + "]";
    }
}
