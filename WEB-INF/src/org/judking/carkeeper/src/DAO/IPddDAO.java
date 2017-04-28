package org.judking.carkeeper.src.DAO;

import org.apache.ibatis.annotations.Param;
import org.judking.carkeeper.src.model.PddDataModel;
import org.judking.carkeeper.src.model.PerformanceModel;
import org.judking.carkeeper.src.model.RouteModel;
import org.judking.carkeeper.src.model.VinDetailModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IPddDAO {
    int insertPddData(PddDataModel pddDataModel);

    int insertRoute(RouteModel routeModel);

    int insertPerformance(PerformanceModel performanceModel);

    List<PerformanceModel> selectNearlyPerformance(@Param("vin") String vin, @Param("limit") int limit);

    List<String> selectAllVin();

    Date selectStartTimeByRouteId(@Param("route_id") String route_id);

    List<RouteModel> selectAllRouteByVin(@Param("vin") String vin);

    List<RouteModel> selectRouteByVinId(@Param("start_time") long start_time, @Param("vin") String vin);

    List<PddDataModel> selectAllPddDataByRoute(@Param("route_id") String route_id);

    List<PddDataModel> selectAllPddDataByRouteNCmd(
            @Param("route_id") String route_id,
            @Param("cmd") String cmd);

    List<PddDataModel> selectSpecifiedPddData(
            @Param("route_id") String route_id,
            @Param("cmd") String cmd,
            @Param("route_begin_time") String route_begin_time,
            @Param("start_time") long start_time,
            @Param("end_time") long end_time);

    List<String> selectSupportedCmdByRouteId(@Param("route_id") String route_id);

    List<String> selectVinByUserId(@Param("user_id") String user_id);

    List<String> selectLevel1CompressRoutes();

    List<String> selectLevel2CompressRoutes();

    int deleteByPddId(@Param("pdd_id") String pdd_id);

    int insertVinDetail(VinDetailModel vinDetailModel);

    VinDetailModel selectVinDetailByVin(@Param("vin") String vin);

    int updateVinDetailByVin(VinDetailModel vinDetailModel);

    List<String> selectOtherSameVins(@Param("vin") String vin);

    String calAvgByRoutesNCmd(Map<String, Object> params);
}
