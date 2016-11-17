package org.judking.carkeeper.src.DAO;

import org.apache.ibatis.annotations.Param;
import org.judking.carkeeper.src.model.PddDataModel;
import org.judking.carkeeper.src.model.RouteModel;
import org.judking.carkeeper.src.model.VinDetailModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IPddDAO {
    public int insertPddData(PddDataModel pddDataModel);

    public int insertRoute(RouteModel routeModel);

    public List<String> selectAllVin();

    public Date selectStartTimeByRouteId(@Param("route_id") String route_id);

    public List<RouteModel> selectAllRouteByVin(@Param("vin") String vin);

    public List<PddDataModel> selectAllPddDataByRoute(@Param("route_id") String route_id);

    public List<PddDataModel> selectAllPddDataByRouteNCmd(
            @Param("route_id") String route_id,
            @Param("cmd") String cmd);

    public List<PddDataModel> selectSpecifiedPddData(
            @Param("route_id") String route_id,
            @Param("cmd") String cmd,
            @Param("route_begin_time") String route_begin_time,
            @Param("start_time") long start_time,
            @Param("end_time") long end_time);

    public List<String> selectSupportedCmdByRouteId(@Param("route_id") String route_id);

    public List<String> selectVinByUserId(@Param("user_id") String user_id);

    public List<String> selectLevel1CompressRoutes();

    public List<String> selectLevel2CompressRoutes();

    public int deleteByPddId(@Param("pdd_id") String pdd_id);

    public int insertVinDetail(VinDetailModel vinDetailModel);

    public VinDetailModel selectVinDetailByVin(@Param("vin") String vin);

    public int updateVinDetailByVin(VinDetailModel vinDetailModel);

    public List<String> selectOtherSameVins(@Param("vin") String vin);

    public String calAvgByRoutesNCmd(Map<String, Object> params);
}
