package org.judking.carkeeper.src.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.judking.carkeeper.src.bean.CommandTransmitter;
import org.judking.carkeeper.src.model.PddDataModel;
import org.judking.carkeeper.src.service.PddDataService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping("/upload")
public class UploadController {
	@Autowired
	@Qualifier("pddDataService")
	private PddDataService pddDataService;
	
	@RequestMapping(method=RequestMethod.POST, value="/pdd/")
	public synchronized String pdd(HttpServletRequest request, 
															 ModelMap modelMap
								) throws IOException		{

		//get encoded input byte
		InputStream inputStream = request.getInputStream();
		List<Byte> encode_bytes = new ArrayList<>();
		byte[] buffer = new byte[1024];
		int bytesRead;

		while ((bytesRead = inputStream.read(buffer)) > 0) {
			byte[] tmp = new byte[bytesRead];
			System.arraycopy(buffer, 0, tmp, 0, bytesRead);
			encode_bytes.addAll(Arrays.asList(ArrayUtils.toObject(tmp)));
		}
		inputStream.close();
		
		// decode
		String uc = pddDataService.uncompress(ArrayUtils.toPrimitive(encode_bytes.toArray(new Byte[encode_bytes.size()])));

		// parse to object list
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithModifiers(Modifier.TRANSIENT);
		Gson gson = builder.create();

		String[] datas = uc.split("#DlM#");
		//get user_name and private_token
		String userName = datas[0];
		String privateToken = datas[1];
		
		//generate CommandTransmitters
		datas = Arrays.copyOfRange(datas, 2, datas.length);
		List<CommandTransmitter> transmitters = new ArrayList<>();
		for (String s : datas) {
			try {
				//every CommandTransmitter represents a route together with all datas.
				CommandTransmitter transmitter = gson.fromJson(s, CommandTransmitter.class);
				transmitters.add(transmitter);
			} catch (JsonSyntaxException jse) {
				System.out.println("/upload/pdd/: parse json to object seems fail");
				continue;
			}
		}

		//write to DB(`user_vin`, `route`, `pdd_data`)
		pddDataService.insertWholeRoute(transmitters, userName, privateToken);

		return "org/judking/carkeeper/resource/page/state";
	}
}
