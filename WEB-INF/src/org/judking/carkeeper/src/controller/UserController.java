package org.judking.carkeeper.src.controller;

import org.judking.carkeeper.src.log.FileLogger;
import org.judking.carkeeper.src.model.UserModel;
import org.judking.carkeeper.src.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final String TAG = "UserController";
    private static final String statePage = "org/judking/carkeeper/resource/page/state";

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/register/")
    public String register(@RequestParam String username, @RequestParam String passwd, ModelMap modelMap) {
        FileLogger.access("POST", "user/register");

        UserModel userModel = userService.registry(username, passwd);
        String privateToken = userModel.getPrivate_token();

        modelMap.addAttribute("state", privateToken);
        return "org/judking/carkeeper/resource/page/state";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login/")
    public String login(
            @RequestParam String username,
            @RequestParam String passwd,
            ModelMap modelMap) {
        FileLogger.access("POST", "user/login/");

        String privateToken = userService.login(username, passwd);
        if (privateToken.equals(UserService.USER_NOT_EXIST) || privateToken.equals(UserService.VERIFY_FAIL)) {
            modelMap.addAttribute("state", "wrong username or password");
        } else {
            modelMap.addAttribute("state", privateToken);
        }
        return statePage;
    }
}
