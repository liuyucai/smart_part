package cn.lps.controller;

import cn.lps.model.request.CarLeaveReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author liuyucai
 * @Created 2020/11/2
 * @Description
 */
@Slf4j
@Controller
public class loginController {

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(CarLeaveReq carLeaveReq) {
        return "account/login";
    }
}
