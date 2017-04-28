package org.judking.carkeeper.src.service;

import com.google.gson.JsonObject;
import com.leilf.hmm.algorithm.ProbabilityCalculator;
import com.leilf.hmm.corpus.DataEntry;
import com.leilf.hmm.corpus.DataPrepossessing;
import com.leilf.hmm.corpus.Instance;
import com.leilf.hmm.model.HMMModel;
import com.leilf.hmm.util.CommonUtils;
import com.leilf.hmm.util.ObservationHelper;
import org.judking.carkeeper.src.DAO.IPddDAO;
import org.judking.carkeeper.src.bean.RouteBean;
import org.judking.carkeeper.src.model.PddDataModel;
import org.judking.carkeeper.src.model.PerformanceModel;
import org.judking.carkeeper.src.util.CommandEnum;
import org.judking.carkeeper.src.util.Constant;
import org.judking.carkeeper.src.util.DbHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by leilf on 2017/4/27.
 */
@Service("performanceService")
public class PerformanceService {
    private static final double DISCOUNT = 0.2;
    @Autowired
    @Qualifier("IPddDAO")
    private IPddDAO iPddDAO;

    public void insertPerformance(RouteBean routeBean) {
        new PerformanceThread(routeBean).start();
    }

    public String queryPerformance(String vin, int limit) {
        List<PerformanceModel> performanceModels = iPddDAO.selectNearlyPerformance(vin, limit);
        double oxygen = 100;
        double catalyst = 100;
        double inject = 100;

        for (PerformanceModel model : performanceModels) {
            oxygen = oxygen * DISCOUNT + (1 - DISCOUNT) * model.getOxygen();
            catalyst = catalyst * DISCOUNT + (1 - DISCOUNT) * model.getCatalyst();
            inject = inject * DISCOUNT + (1 - DISCOUNT) * model.getInject();
        }

        PerformanceModel performanceModel = new PerformanceModel();
        performanceModel.setCatalyst(catalyst);
        performanceModel.setInject(inject);
        performanceModel.setOxygen(oxygen);
        return toJson(performanceModel);
    }

    private String toJson(PerformanceModel performanceModel) {
        JsonObject json = new JsonObject();
        json.addProperty("oxygen", performanceModel.getOxygen());
        json.addProperty("catalyst", performanceModel.getCatalyst());
        json.addProperty("inject", performanceModel.getInject());
        return json.toString();
    }


    private class PerformanceThread extends Thread {
        private RouteBean routeBean;
        private PerformanceModel performanceModel;

        PerformanceThread(RouteBean routeBean) {
            this.routeBean = routeBean;
            performanceModel = new PerformanceModel();
            performanceModel.setRoute_id(routeBean.getRouteModel().getRoute_id());
        }

        @Override
        public void run() {
            computePerformance();
            insert();
        }

        private void insert() {
            DbHelper.assertGtZero(iPddDAO.insertPerformance(performanceModel));
        }

        private void computePerformance() {
            computeCatalystPerformance();
            computeInjectPerformance();
            computeOxygenPerformance();
        }

        private void computeOxygenPerformance() {
            String[] commands = CommandEnum.OXYGEN_SENSOR;
            if (!exist(commands)) {
                return;
            }
            Map<String, List<Float>> data = genNormalizeData(commands);
            List<Float> oxygenData = data.get(CommandEnum.PID_OXYGEN);
            List<Float> throttleData = data.get(CommandEnum.PID_THROTTLE);
            int[] obs = new int[oxygenData.size()];
            for (int i = 0; i < oxygenData.size(); i++) {
                obs[i] = ObservationHelper.oxygenObservation(oxygenData.get(i), throttleData.get(i));
            }
            performanceModel.setOxygen(computePerformance(obs, Constant.OXYGEN_SENSOR_MODEL_NAME));
        }

        private void computeInjectPerformance() {
            String[] commands = CommandEnum.INJECTOR;
            if (!exist(commands)) {
                return;
            }
            Map<String, List<Float>> data = genNormalizeData(commands);
            List<Float> mafData = data.get(CommandEnum.PID_MAF_FLOW);
            List<Float> fuelData = data.get(CommandEnum.PID_FUEL_PRESSURE);
            int[] obs = new int[mafData.size()];
            for (int i = 0; i < mafData.size(); i++) {
                obs[i] = ObservationHelper.injectorObservation(mafData.get(i), fuelData.get(i));
            }
            performanceModel.setInject(computePerformance(obs, Constant.INJECTOR_MODEL_NAME));
        }

        private void computeCatalystPerformance() {
            String[] commands = CommandEnum.CATALYST_SENSOR;
            if (!exist(commands)) {
                return;
            }
            Map<String, List<Float>> data = genNormalizeData(commands);
            List<Float> voltData = data.get(CommandEnum.PID_OXYGEN);
            List<Float> tempData = data.get(CommandEnum.PID_CATALYST_TEMP);
            int[] obs = new int[voltData.size()];
            for (int i = 0; i < voltData.size(); i++) {
                obs[i] = ObservationHelper.catalystObservation(tempData.get(i), voltData.get(i));
            }
            performanceModel.setCatalyst(computePerformance(obs, Constant.CATALYST_SENSOR_MODEL_NAME));
        }

        private Map<String, List<Float>> genNormalizeData(String[] commands) {
            ArrayList<DataEntry> entries = new ArrayList<>();
            Map<String, List<PddDataModel>> modelMap = routeBean.getCommands();
            for (String command : commands) {
                List<PddDataModel> models = modelMap.get(command);
                float ratio = command.equals(CommandEnum.PID_OXYGEN) ? 256f : 1f;
                for (PddDataModel model : models) {
                    float v = Float.valueOf(model.getRipeData()) / ratio;
                    long x = computeInterval(model);
                    entries.add(new DataEntry(x, v, command));
                }
            }

            DataPrepossessing dataPrepossessing = new DataPrepossessing(commands);
            return dataPrepossessing.convertData(entries);
        }

        private double computePerformance(int[] obs, String name) {
            HMMModel hmmModel = new HMMModel(Constant.BASE_MODEL_FILE_PATH, name);
            ProbabilityCalculator calculator = new ProbabilityCalculator(hmmModel.getParam());
            double log_p = calculator.log_probability(new Instance(obs));
            return CommonUtils.computePerformance(log_p, obs.length, hmmModel.getNrObs());
        }

        private long computeInterval(PddDataModel model) {
            long diff = model.getDate().getTime() - routeBean.getRouteModel().getStart_time().getTime();
            return diff / 1000;
        }

        private boolean exist(String[] commands) {
            if (routeBean == null || routeBean.getCommands() == null) {
                return false;
            }

            Map<String, List<PddDataModel>> models = routeBean.getCommands();
            for (String command : commands) {
                if (!models.containsKey(command)) {
                    return false;
                }
            }

            return true;
        }

    }
}
