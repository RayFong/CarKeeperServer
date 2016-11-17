package org.judking.carkeeper.src.service;

import org.judking.carkeeper.src.DAO.IPddDAO;
import org.judking.carkeeper.src.DAO.IUserDAO;
import org.judking.carkeeper.src.bean.CommandTransmitter;
import org.judking.carkeeper.src.bean.OneKeyCmpTransmitter;
import org.judking.carkeeper.src.model.PddDataModel;
import org.judking.carkeeper.src.model.RouteModel;
import org.judking.carkeeper.src.model.UserModel;
import org.judking.carkeeper.src.model.VinDetailModel;
import org.judking.carkeeper.src.util.DbHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;

@Service("pddDataService")
public class PddDataService {
    @Autowired
    @Qualifier("IPddDAO")
    private IPddDAO iPddDAO;
    @Autowired
    @Qualifier("IUserDAO")
    private IUserDAO iUserDAO;
    @Autowired
    @Qualifier("transactionManager")
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    @Qualifier("resourceService")
    private ResourceService resourceService;

    private Map<String, String> cmdNameMap;

    // --pdd post
    public void insertWholeRoute(List<CommandTransmitter> transmitters,
                                 String userName,
                                 String privateToken) {
        String vin = transmitters.get(0).getVin();

        // begin a transaction for registering to DB
        TransactionStatus status = dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            if (userName.equals("null") == false) {
                //check data integrity of user name and private token.
                UserModel userModel = iUserDAO.verifyUserName(userName, privateToken);
                if (userModel != null) {
                    //check whether the current user is associated with vin
                    //if not, write this association into `user_vin` table
                    Integer user_vin_id = iUserDAO.CheckUserVin(userModel.getUser_id(), vin);
                    if (user_vin_id == null) {
                        iUserDAO.insertUserVin(userModel.getUser_id(), vin);
                    }
                }
            }

            // write into `route` table
            Date start_time = transmitters.get(0).getDate();
            String duration = String.valueOf(Math.round((transmitters.get(transmitters.size() - 1).getDate().getTime() - start_time.getTime()) / 1000));
            RouteModel routeModel = new RouteModel(vin, start_time, duration);
            DbHelper.assertGtZero(iPddDAO.insertRoute(routeModel));

            // write into `pdd_data` table
            for (CommandTransmitter ct : transmitters) {
                List<PddDataModel> pddDataModels = genPddDataModel(ct, routeModel.getRoute_id());
                insertPddData(pddDataModels);
            }

            dataSourceTransactionManager.commit(status);
        } catch (RuntimeException e) {
            dataSourceTransactionManager.rollback(status);
            throw e;
        }

    }

    private static List<PddDataModel> genPddDataModel(
            CommandTransmitter transmitter, String route_id) {
        List<PddDataModel> pddDataModels = new ArrayList<PddDataModel>();
        for (PddDataModel pdm : transmitter.getCommands().values()) {
            pdm.setRoute_id(route_id);
            pdm.setDate(transmitter.getDate());
            pddDataModels.add(pdm);
        }

        return pddDataModels;
    }

    private void insertPddData(List<PddDataModel> pddDataModels) {
        for (PddDataModel pddDataModel : pddDataModels) {
            // insert pddDataModel into pdd_data table
            DbHelper.assertGtZero(iPddDAO.insertPddData(pddDataModel));
        }
    }

    // 解压缩
    public String uncompress(byte[] str) throws IOException {
        if (str == null || str.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str);
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        // toString()使用平台默认编码，也可以显式的指定如toString("GBK")
        return out.toString();
    }

    /**
     * 返回命令对应名称
     * 不区分大小写
     *
     * @param cmd 类似0103的命令
     * @return 命令名称; 如果命令不存在:null
     */
    public String getCmdName(String cmd) {
        String name = cmdNameMap.get(cmd.toLowerCase());
        name = (name == null ? cmdNameMap.get(cmd.toUpperCase()) : name);
        return name;
    }

    /**
     * 通过Cmd列表，生成对应的命令名称Map
     *
     * @param cmds
     * @return
     */
    public Map<String, String> supplyCmdNames(List<String> cmds) {
        Map<String, String> cmdNames = new LinkedHashMap<>();
        for (String cmd : cmds) {
            String name = getCmdName(cmd);
            if (name != null) {
                cmdNames.put(cmd, name);
            }
        }

        return cmdNames;
    }

    public String getFmtRouteBeginTime(String route_id) {
        //generate route_begin_time
        Date route_begin_time = iPddDAO.selectStartTimeByRouteId(route_id);
        String fmt_route_begin_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(route_begin_time);
        return fmt_route_begin_time;
    }

    /**
     * 生成各个限制条件下的Pdd Data
     * start_time, end_time为两个相对时间点。例如某route总时间1800秒，
     * 如果start_time=30, end_time=1000,则返回的数据为此行程开始后30秒到1000秒
     * 之间记录下来的数据
     *
     * @param route_id   某行程id
     * @param cmd        数据类型
     * @param start_time 行程开始多少秒后记录的数据
     * @param end_time   行程开始多少秒前记录的数据
     * @return
     */
    public List<PddDataModel> getSpecificData(String route_id,
                                              String fmt_route_begin_time,
                                              String cmd,
                                              long start_time,
                                              long end_time) {
        List<PddDataModel> pddDataModels = iPddDAO.selectSpecifiedPddData(route_id, cmd, fmt_route_begin_time, start_time, end_time);
        return pddDataModels;
    }

    /**
     * 将Pdd Data转为xcharts可识别的json字符串
     *
     * @param datas             pdd数据
     * @param fmtRouteBeginTime route开始时间
     * @return
     */
    public String formatPddDataToJson(List<PddDataModel> datas, String fmtRouteBeginTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"xScale\": \"linear\",\"yScale\": \"linear\",\"main\": [{\"data\": [");
        try {
            Date routeBeginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fmtRouteBeginTime);

            for (PddDataModel data : datas) {
                long x = returnDiffInSecond(routeBeginTime, data.getDate());
                String y = data.getRipeData();
                sb.append("{\"x\":").append(x).append(",\"y\":").append(y).append("},");
            }
            sb.deleteCharAt(sb.length() - 1);

            sb.append("]}]}");
            return sb.toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算两个Date相差的秒数
     *
     * @param d1
     * @param d2
     * @return
     */
    private long returnDiffInSecond(Date d1, Date d2) {
        return (d2.getTime() - d1.getTime()) / 1000;
    }

    /**
     * 压缩Pdd数据
     */
    public void compress() {
        //-----level 1 compress-------
        //get route_ids needed to conpress
        List<String> routesInLevel1 = iPddDAO.selectLevel1CompressRoutes();
        for (String route : routesInLevel1) {
            //get all supported cmds according to specified route_id
            List<String> supportedCmds = iPddDAO.selectSupportedCmdByRouteId(route);
            for (String cmd : supportedCmds) {
                //get all pdd datas according to specified route_id and cmd
                List<PddDataModel> pdds = iPddDAO.selectAllPddDataByRouteNCmd(route, cmd);
                List<String> pdd_ids = getPddIdToDelete(pdds, 30);
                delPddId(pdd_ids);
            }
        }

        //-----level 2 compress-------
        //get route_ids needed to conpress
        List<String> routesInLevel2 = iPddDAO.selectLevel2CompressRoutes();
        for (String route : routesInLevel2) {
            //get all supported cmds according to specified route_id
            List<String> supportedCmds = iPddDAO.selectSupportedCmdByRouteId(route);
            for (String cmd : supportedCmds) {
                //get all pdd datas according to specified route_id and cmd
                List<PddDataModel> pdds = iPddDAO.selectAllPddDataByRouteNCmd(route, cmd);
                List<String> pdd_ids = getPddIdToDelete(pdds, 60);
                delPddId(pdd_ids);
            }
        }

    }

    /**
     * @param pdds
     * @param interval 每两个pdd数据最小间隔时间
     * @return
     */
    private List<String> getPddIdToDelete(List<PddDataModel> pdds, int interval) {
        List<String> rtn = new ArrayList<>();
        PddDataModel TAG = pdds.get(0);
        for (int i = 1; i < pdds.size(); ++i) {
            PddDataModel cur = pdds.get(i);
            if (returnDiffInSecond(TAG.getDate(), cur.getDate()) >= interval) {
                TAG = cur;
            } else {
                rtn.add(cur.getPdd_id());
            }
        }
        return rtn;
    }

    private void delPddId(List<String> pdd_ids) {
        // begin a transaction for registering to DB
        TransactionStatus status = dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            for (String pdd_id : pdd_ids) {
                iPddDAO.deleteByPddId(pdd_id);
            }

            dataSourceTransactionManager.commit(status);
        } catch (RuntimeException e) {
            dataSourceTransactionManager.rollback(status);
        }
    }

    public void setVinDetail(VinDetailModel vinDetailModel) {
        // begin a transaction for registering to DB
        TransactionStatus status = dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            VinDetailModel tmp = iPddDAO.selectVinDetailByVin(vinDetailModel.getVin());
            if (tmp == null) {
                iPddDAO.insertVinDetail(vinDetailModel);
            } else {
                iPddDAO.updateVinDetailByVin(vinDetailModel);
            }

            dataSourceTransactionManager.commit(status);
        } catch (RuntimeException e) {
            dataSourceTransactionManager.rollback(status);
        }
    }

    public Map<String, OneKeyCmpTransmitter> oneKeyCmp(String currentVin) {
        //-------分别生成当前vin、其他相同属性vins的route_id列表--------
        //其他相同车型、里程数的vin
        List<String> otherVins = iPddDAO.selectOtherSameVins(currentVin);
        //所有这些vin对应的route_id列表
        List<String> otherVinsRouteIds = new ArrayList<>();
        for (String vin : otherVins) {
            List<RouteModel> routes = iPddDAO.selectAllRouteByVin(vin);
            for (RouteModel route : routes) {
                otherVinsRouteIds.add(route.getRoute_id());
            }
        }
        //当前vin对应的route_id列表
        List<String> currentVinRouteIds = new ArrayList<>();
        List<RouteModel> currentRoutes = iPddDAO.selectAllRouteByVin(currentVin);
        for (RouteModel route : currentRoutes) {
            currentVinRouteIds.add(route.getRoute_id());
        }

        //当前vin支持的cmd类型列表
        List<String> cmds = iPddDAO.selectSupportedCmdByRouteId(currentVinRouteIds.get(0));

        //-------计算同cmd下当前vin和其他vin的数据平均值--------
        Map<String, OneKeyCmpTransmitter> rtn = new HashMap<>();    //Key: cmd, Value: OneKeyCmpTransmitter
        for (String cmd : cmds) {
            Map<String, Object> map = new HashMap<>();
            map.put("list", otherVinsRouteIds);
            map.put("cmd", cmd);
            String otherAvg = iPddDAO.calAvgByRoutesNCmd(map);

            map.put("list", currentVinRouteIds);
            String currentAvg = iPddDAO.calAvgByRoutesNCmd(map);

            rtn.put(cmd, new OneKeyCmpTransmitter(cmd, currentAvg, otherAvg));
        }

        return rtn;
    }

    //------------------------------PostComstruct-------------------------------------
    @PostConstruct
    private void initCmdNameMap() {
        if (cmdNameMap != null) {
            return;
        }

        cmdNameMap = new HashMap<>();
        File file = resourceService
                .get("classpath:org/judking/carkeeper/resource/raw/PDDObdCmdConfig.txt");
        String line;
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            while ((line = in.readLine()) != null) {
                String[] field = splitCfgLine(line);
                cmdNameMap.put("01" + field[1], field[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] splitCfgLine(String info) {
        info = info.trim();
        info = info.replaceAll("(\t){2,}", "\t");
        return info.split("\t");
    }
}
