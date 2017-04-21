package org.judking.carkeeper.src.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by leilf on 2017/4/19.
 */
@Controller
@RequestMapping("model")
public class DownloadController {
    private static final String state_page = "org/judking/carkeeper/resource/page/state";
    private static final String modelPath = System.getProperty("user.dir") + File.separator + ".." + File.separator + ".." +
            File.separator + "model" + File.separator;

    @RequestMapping("/test/")
    public String test(HttpServletRequest request, ModelMap modelMap) {
        modelMap.addAttribute("state", modelPath);
        return state_page;
    }

    @RequestMapping(value = "/download/")
    public void download(@RequestParam String filename, HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
        try {
            InputStream in = new FileInputStream(new File(modelPath + filename));
            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[2048];
            int length = 0;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
