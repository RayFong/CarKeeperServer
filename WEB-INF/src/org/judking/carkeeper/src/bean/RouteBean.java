package org.judking.carkeeper.src.bean;

import org.judking.carkeeper.src.model.PddDataModel;
import org.judking.carkeeper.src.model.RouteModel;

import java.util.List;
import java.util.Map;

/**
 * Created by leilf on 2017/3/7.
 */
public class RouteBean {
    private String user;
    private String token;
    private RouteModel routeModel;
    private Map<String, List<PddDataModel>> commands;

    public Map<String, List<PddDataModel>> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, List<PddDataModel>> commands) {
        this.commands = commands;
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

    @Override
    public String toString() {
        return "RouteBean [user=" + user + ", token=" + token + ", commands=" + commands + "]";
    }

    public RouteModel getRouteModel() {
        return routeModel;
    }

    public void setRouteModel(RouteModel routeModel) {
        this.routeModel = routeModel;
    }
}
