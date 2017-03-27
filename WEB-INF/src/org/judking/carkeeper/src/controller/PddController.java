package org.judking.carkeeper.src.controller;

import org.judking.carkeeper.src.DAO.IPddDAO;
import org.judking.carkeeper.src.bean.PddDataBean;
import org.judking.carkeeper.src.log.FileLogger;
import org.judking.carkeeper.src.model.PddDataModel;
import org.judking.carkeeper.src.model.RouteModel;
import org.judking.carkeeper.src.model.UserModel;
import org.judking.carkeeper.src.model.VinDetailModel;
import org.judking.carkeeper.src.service.PddDataService;
import org.judking.carkeeper.src.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pdd")
public class PddController {
    private static final String state_page = "org/judking/carkeeper/resource/page/state";
    @Autowired
    @Qualifier("userService")
    UserService userService;
    @Autowired
    @Qualifier("pddDataService")
    private PddDataService pddDataService;
    @Autowired
    @Qualifier("IPddDAO")
    private IPddDAO iPddDAO;

    @RequestMapping(method = RequestMethod.GET, value = "/vin/")
    public String vins(HttpServletRequest request,
                       ModelMap modelMap
    ) {
        FileLogger.access("GET", "/pdd/vin/");

        UserModel userModel = userService.tryGetCurrentLoginUserModel();
        if (userModel == null) {
            modelMap.addAttribute("state", "not login yet.");
            return "org/judking/carkeeper/resource/page/state";
        }
        List<String> vins = iPddDAO.selectVinByUserId(userModel.getUser_id());
        List<VinDetailModel> vinDetails = new ArrayList<>();
        for (String vin : vins) {
            vinDetails.add(iPddDAO.selectVinDetailByVin(vin));
        }

        modelMap.addAttribute("vins", vins);
        modelMap.addAttribute("details", vinDetails);
        return "forward:/pdd/getVins.jsp";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/vin/setDetail/")
    public String setVinInfo(HttpServletRequest request,
                             @RequestParam String vin,
                             @RequestParam String car_type,
                             @RequestParam String miles,
                             ModelMap modelMap
    ) {

        VinDetailModel vinDetailModel = new VinDetailModel();
        vinDetailModel.setVin(vin);
        vinDetailModel.setCar_type(car_type);
        vinDetailModel.setMiles(miles);
        pddDataService.setVinDetail(vinDetailModel);

        modelMap.addAttribute("state", "");
        return "org/judking/carkeeper/resource/page/state";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/vin/oneKeyCmp/")
    public String oneKeyCmp(HttpServletRequest request,
                            @RequestParam String vin,
                            ModelMap modelMap
    ) {


        modelMap.addAttribute("state", "");
        return "org/judking/carkeeper/resource/page/state";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/vin/{vin}")
    public String routes(HttpServletRequest request,
                         @PathVariable String vin,
                         ModelMap modelMap
    ) {

        modelMap.addAttribute("vin", vin);
        List<RouteModel> routeModels = iPddDAO.selectAllRouteByVin(vin);
        modelMap.addAttribute("routes", routeModels);
        return "forward:/pdd/getRoutes.jsp";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/vin/{vin}/route/{route_id}")
    public String pdds(HttpServletRequest request,
                       @PathVariable String vin,
                       @PathVariable String route_id,
                       ModelMap modelMap
    ) {

        List<String> cmds = iPddDAO.selectSupportedCmdByRouteId(route_id);
        Map<String, String> cmdNames = pddDataService.supplyCmdNames(cmds);

        modelMap.addAttribute("cmds", cmdNames);
        modelMap.addAttribute("route_id", route_id);
        return "forward:/pdd/getPddDatas.jsp";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getPddData/")
    public String getPddData(
            @RequestParam String route_id,
            @RequestParam String cmd,
            @RequestParam long start_time,
            @RequestParam long end_time,
            ModelMap modelMap) {

        String fmtRouteBeginTime = pddDataService.getFmtRouteBeginTime(route_id);
        List<PddDataModel> datas = pddDataService.getSpecificData(route_id, fmtRouteBeginTime, cmd, start_time, end_time);
        String json = pddDataService.formatPddDataToJson(datas, fmtRouteBeginTime);


//		String json = "{\"xScale\": \"time\",\"yScale\": \"linear\",\"main\": [{\"data\": [{\"x\":\"0\",\"y\":30.0},{\"x\":\"21\",\"y\":30.0}]}]}";
        modelMap.addAttribute("state", json);
        return "org/judking/carkeeper/resource/page/state";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/queryPddData/")
    public String queryPddData(@RequestParam long start_time, @RequestParam String vin, @RequestParam String cmd, ModelMap modelMap) {
        List<RouteModel> routes = iPddDAO.selectRouteByVinId(start_time, vin);
        if (routes != null && !routes.isEmpty()) {
            List<PddDataBean> datas = pddDataService.getPddDataFromRouteId(routes.get(0).getRoute_id(), cmd);
            String json = pddDataService.toJson(datas, routes.get(0).getStart_time().getTime());
            modelMap.addAttribute("state", json);
        }
        return state_page;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/compress/")
    public String compressPddData(
            ModelMap modelMap) {


        pddDataService.compress();
        return "org/judking/carkeeper/resource/page/state";
    }
}
