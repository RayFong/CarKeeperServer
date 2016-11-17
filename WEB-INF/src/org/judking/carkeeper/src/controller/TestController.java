package org.judking.carkeeper.src.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.judking.carkeeper.src.DAO.IPddDAO;
import org.judking.carkeeper.src.DAO.IUserDAO;
import org.judking.carkeeper.src.bean.CommandTransmitter;
import org.judking.carkeeper.src.model.PddDataModel;
import org.judking.carkeeper.src.model.RouteModel;
import org.judking.carkeeper.src.model.UserModel;
import org.judking.carkeeper.src.model.VinDetailModel;
import org.judking.carkeeper.src.service.PddDataService;
import org.judking.carkeeper.src.service.ResourceService;
import org.judking.carkeeper.src.service.UserService;
import org.judking.carkeeper.src.util.DbHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UrlPathHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.bcel.internal.generic.ISUB;

@Controller
@RequestMapping("/test")
public class TestController {
	@Autowired
	@Qualifier("IPddDAO")
	private IPddDAO iPddDAO;
	@Autowired
	@Qualifier("pddDataService")
	private PddDataService pddDataService;
	@Autowired
	@Qualifier("transactionManager")
	private DataSourceTransactionManager dataSourceTransactionManager;
	@Autowired
	@Qualifier("resourceService")
	private ResourceService resourceService;
	@Autowired
	@Qualifier("userService")
	UserService userService;
	@Autowired
	@Qualifier("IUserDAO")
	private IUserDAO iUserDAO;
	
	@RequestMapping(method=RequestMethod.POST, value="/test/")
	public String test(
			@RequestParam String cmd,
			@RequestParam String start_time,
			@RequestParam String end_time,
			ModelMap modelMap) 	{
//		String json = "{\"commands\":{\"0103\":{\"cmd\":\"0103\",\"ripeData\":\"no data \r\r\"},\"0104\":{\"cmd\":\"0104\",\"ripeData\":50.19607808},\"0105\":{\"cmd\":\"0105\",\"ripeData\":41.0},\"010a\":{\"cmd\":\"010a\",\"ripeData\":3.0},\"010b\":{\"cmd\":\"010b\",\"ripeData\":41.0},\"010c\":{\"cmd\":\"010c\",\"ripeData\":10000.0},\"010d\":{\"cmd\":\"010d\",\"ripeData\":200.0},\"010e\":{\"cmd\":\"010e\",\"ripeData\":1.0},\"010f\":{\"cmd\":\"010f\",\"ripeData\":-33.0},\"0110\":{\"cmd\":\"0110\",\"ripeData\":154.20000000000002},\"0111\":{\"cmd\":\"0111\",\"ripeData\":22.74509788},\"011f\":{\"cmd\":\"011f\",\"ripeData\":406.0},\"0121\":{\"cmd\":\"0121\",\"ripeData\":128.0},\"0122\":{\"cmd\":\"0122\",\"ripeData\":2588.593},\"0123\":{\"cmd\":\"0123\",\"ripeData\":327670.0},\"012c\":{\"cmd\":\"012c\",\"ripeData\":50.19607808},\"012d\":{\"cmd\":\"012d\",\"ripeData\":1.5625},\"012e\":{\"cmd\":\"012e\",\"ripeData\":51.76470552},\"012f\":{\"cmd\":\"012f\",\"ripeData\":58.823529},\"0130\":{\"cmd\":\"0130\",\"ripeData\":3.0},\"0131\":{\"cmd\":\"0131\",\"ripeData\":30.0},\"0132\":{\"cmd\":\"0132\",\"ripeData\":-8192.0},\"0133\":{\"cmd\":\"0133\",\"ripeData\":136.0},\"013c\":{\"cmd\":\"013c\",\"ripeData\":2.0},\"013d\":{\"cmd\":\"013d\",\"ripeData\":3.0},\"013e\":{\"cmd\":\"013e\",\"ripeData\":4.600000000000001},\"013f\":{\"cmd\":\"013f\",\"ripeData\":6.200000000000003},\"0142\":{\"cmd\":\"0142\",\"ripeData\":60.0},\"0143\":{\"cmd\":\"0143\",\"ripeData\":199.9999986},\"0144\":{\"cmd\":\"0144\",\"ripeData\":0.9999833060000001},\"0145\":{\"cmd\":\"0145\",\"ripeData\":56.47058784},\"0146\":{\"cmd\":\"0146\",\"ripeData\":106.0},\"0147\":{\"cmd\":\"0147\",\"ripeData\":50.19607808},\"0148\":{\"cmd\":\"0148\",\"ripeData\":50.9803918},\"0149\":{\"cmd\":\"0149\",\"ripeData\":51.76470552},\"014a\":{\"cmd\":\"014a\",\"ripeData\":52.54901924},\"014b\":{\"cmd\":\"014b\",\"ripeData\":53.33333296},\"014c\":{\"cmd\":\"014c\",\"ripeData\":54.11764668},\"014d\":{\"cmd\":\"014d\",\"ripeData\":60.0},\"014e\":{\"cmd\":\"014e\",\"ripeData\":63.0},\"0152\":{\"cmd\":\"0152\",\"ripeData\":53.33333296},\"0153\":{\"cmd\":\"0153\",\"ripeData\":200.0},\"0154\":{\"cmd\":\"0154\",\"ripeData\":-32747.0},\"0159\":{\"cmd\":\"0159\",\"ripeData\":70000.0},\"015a\":{\"cmd\":\"015a\",\"ripeData\":52.54901924}},\"date\":\"Apr 5, 2013 1:37:57 PM\",\"vin\":\"LSGUA83B5BE055614\"}";
//		GsonBuilder builder = new GsonBuilder();
//		builder.excludeFieldsWithModifiers(Modifier.TRANSIENT);
//		Gson gson = builder.create();
//		CommandTransmitter transmitter = gson.fromJson(json, CommandTransmitter.class);
//		
//		PddDataModel pddDataModel = PddDataModelGen.GEN(transmitter);
//		
//		// insert pddDataModel into pdd_data table
//		DbHelper.assertGtZero(iPddDAO.insertPddData(pddDataModel));
		
//		int i = iPddDAO.insertRoute("123", new Date(), "312");
//		System.out.println("test done"+i);
		
//		int i = iPddDAO.insertRoute("123", new Date(), "312");
		
		// begin a transaction for registering to DB
//		TransactionStatus status = dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition());
//		try {
//			
//			PddDataModel pdm = new PddDataModel();
//			pdm.setDate(new Date());
//			pdm.setCmd("0102");
//			pdm.setRipeData("no data");
//			pdm.setRoute_id(String.valueOf(19));
//			int pdd_id = iPddDAO.insertPddData(pdm);
//			System.out.println(pdd_id);
//			
//			dataSourceTransactionManager.commit(status);
//		} catch (RuntimeException e) {
//			dataSourceTransactionManager.rollback(status);
//			throw e;
//		}
		
//		iPddDAO.delRoute("9");
		
//		List<String> vins = iPddDAO.selectAllVin();
//		System.out.println(vins);
		
//		List<RouteModel> routes = iPddDAO.selectAllRouteByVin("LSGUA83B5BE055614");
//		System.out.println(routes);
		
//		List<PddDataModel> pddDatas = iPddDAO.selectAllPddDataByRoute("32");
//		System.out.println(pddDatas);

		
		System.out.println(cmd+" "+start_time+" "+end_time);
		String json = "{\"xScale\": \"time\",\"yScale\": \"linear\",\"main\": [{\"className\": \".pizza\",\"data\": [{\"x\": \"2012-11-05\",\"y\": 1},{\"x\": \"2012-11-06\",\"y\": 6},{\"x\": \"2012-11-07\",\"y\": 1}]}]}";
		modelMap.addAttribute("state", json);
		
		
		return "org/judking/carkeeper/resource/page/state";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/test1/")
	public String test1(HttpServletRequest request, HttpServletResponse response) 	{
//		try {
//            InputStream inputStream = request.getInputStream();
// 
//            if (inputStream != null) {
//                byte[] buffer = new byte[1024];
//               List<Byte> list = new ArrayList<>();
//                int bytesRead;
// 
//                while ((bytesRead = inputStream.read(buffer)) > 0) {
//                	 byte[] tmp = new byte[bytesRead];
//                	 System.arraycopy(buffer, 0, tmp, 0, bytesRead);
//                    list.addAll(Arrays.asList(ArrayUtils.toObject(tmp)));
//                }
//                String uc = uncompress(ArrayUtils.toPrimitive(list.toArray(new Byte[list.size()])));
//                System.out.println(uc);
//                inputStream.close();
//            }
//        } catch (IOException e) {
//        	e.printStackTrace();
//        }
		
		
//		List<PddDataModel> pddDataModels = iPddDAO.selectSpecifiedPddData("32", "0131", "2013-04-13 17:12:06", "0", "30");
//		System.out.println(pddDataModels);
		
//		Date start_time = iPddDAO.selectStartTimeByRouteId("32");
//		String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start_time);
//		System.out.println(s);
		
//		UserModel userModel = new UserModel("judking", "qweasd");
//		String s = Integer.toHexString(userModel.hashCode());
//		UserModel userModel1 = new UserModel("judking", "qwe1asd");
//		String s1 = Integer.toHexString(userModel.hashCode());
//		userModel.setPrivate_token("pri46");
//		iUserDAO.insertUser(userModel);
//		System.out.println(s);
//		System.out.println(s1);
		
//		System.out.println(iUserDAO.insertUserVin("11", "1234567"));
//		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^");
//		System.out.println(iUserDAO.insertUserVin("12", "7654321"));
		
//		System.out.println(iUserDAO.CheckUserVin("111", "1234567"));
//		System.out.println(iUserDAO.insertUserVin("11", "qqq"));
		
//		UserModel userModel = userService.tryGetCurrentLoginUserModel();
//		System.out.println(userModel);
		
//		List<String> routes = iPddDAO.selectLevel1CompressRoutes();
//		System.out.println(routes);
		
//		pddDataService.compress();
		
//		VinDetailModel vinDetailModel = new VinDetailModel();
//		vinDetailModel.setCar_type("qqq");
//		vinDetailModel.setMiles("100");
//		vinDetailModel.setVin("test1");
//		iPddDAO.insertVinDetail(vinDetailModel);
		
//		VinDetailModel vinDetailModel = iPddDAO.selectVinDetailByVin("test1");
//		vinDetailModel.setMiles("1000");
//		vinDetailModel.setCar_type("www");
//		iPddDAO.updateVinDetailByVin(vinDetailModel);
		
//		List<String> list = new ArrayList<>();
//		list.add("33");
//		list.add("35");
//		Map<String, Object> map = new HashMap<>();
//		map.put("list", list);
//		map.put("cmd", "0105");
//		String s = iPddDAO.calAvgByRoutesNCmd(map);
//		System.out.println(s);
		
		System.out.println(iPddDAO.selectOtherSameVins("test1"));
		
		
		
		return "org/judking/carkeeper/resource/page/state";
	}
	
	public static String uncompress(byte[] bytes) {
	if (bytes == null || bytes.length == 0) {
		return null;
	}
	try {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		// toString()使用平台默认编码，也可以显式的指定如toString("GBK")
		return out.toString();
	} catch (IOException ie) {

	}
	return null;
}

}
