package org.judking.carkeeper.src.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.leilf.database.DBInterface;
import com.leilf.database.model.LocationModel;
import com.leilf.database.model.RoadModel;
import com.leilf.graph.Point;
import com.leilf.hmm.corpus.DataEntry;
import com.leilf.search.RoadMap;
import com.leilf.search.RouteSearch;
import org.judking.carkeeper.src.DAO.IPddDAO;
import org.judking.carkeeper.src.bean.RouteBean;
import org.judking.carkeeper.src.model.PddDataModel;
import org.judking.carkeeper.src.util.CommandEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leilf on 2017/4/30.
 */
@Service("routeSearchService")
public class RouteSearchService {
    private final IPddDAO iPddDAO;
    private DBInterface db = new DBInterface() {
        @Override
        public int insertLocation(LocationModel locationModel) {
            return iPddDAO.insertLocation(locationModel);
        }

        @Override
        public int insertRoad(RoadModel roadModel) {
            return iPddDAO.insertRoad(roadModel);
        }

        @Override
        public int updateLocation(LocationModel locationModel) {
            return iPddDAO.updateLocation(locationModel);
        }

        @Override
        public int updateRoad(RoadModel roadModel) {
            return iPddDAO.updateRoad(roadModel);
        }

        @Override
        public List<LocationModel> queryLocations(LocationModel l1, LocationModel l2) {
            return iPddDAO.queryLocations(l1.getLatitude(), l2.getLatitude(), l1.getLongitude(), l2.getLongitude());
        }

        @Override
        public List<RoadModel> queryRoads(List<LocationModel> locationModels) {
            List<Integer> locations = new ArrayList<>();
            for (LocationModel locationModel : locationModels) {
                locations.add(locationModel.getId());
            }
            Map<String, List<Integer>> param = new HashMap<>();
            param.put("locations", locations);
            return iPddDAO.queryRoads(param);
        }
    };

    @Autowired
    public RouteSearchService(@Qualifier("IPddDAO") IPddDAO iPddDAO) {
        this.iPddDAO = iPddDAO;
    }

    public void buildRoadMap(RouteBean routeBean) {
        new RoadMapThread(routeBean).start();
    }

    public List<Point> search(LocationModel start, LocationModel end, int type) {
        RouteSearch routeSearch = new RouteSearch(db, start, end, type);
        return routeSearch.search();
    }

    public String formatLocationToJson(List<Point> points) {
        JsonArray jsonArray = new JsonArray();
        for (Point point : points) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("x", point.getX());
            jsonObject.addProperty("y", point.getY());
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }

    private class RoadMapThread extends Thread {
        private RouteBean routeBean;
        private RoadMap roadMap;

        RoadMapThread(RouteBean routeBean) {
            this.routeBean = routeBean;
            this.roadMap = new RoadMap(db);
        }

        @Override
        public void run() {
            ArrayList<DataEntry> data = queryRawData();
            roadMap.build(data);
        }

        private ArrayList<DataEntry> queryRawData() {
            String[] commands = {CommandEnum.PID_SPEED, CommandEnum.LATITUDE, CommandEnum.LONGITUDE, CommandEnum.FUEL_RATE};

            ArrayList<DataEntry> entries = new ArrayList<>();
            Map<String, List<PddDataModel>> modelMap = routeBean.getCommands();
            for (String command : commands) {
                List<PddDataModel> models = modelMap.get(command);
                for (PddDataModel model : models) {
                    float v = Float.valueOf(model.getRipeData());
                    long x = computeInterval(model);
                    entries.add(new DataEntry(x, v, command));
                }
            }
            return entries;
        }

        private long computeInterval(PddDataModel model) {
            long diff = model.getDate().getTime() - routeBean.getRouteModel().getStart_time().getTime();
            return diff / 1000;
        }
    }
}
