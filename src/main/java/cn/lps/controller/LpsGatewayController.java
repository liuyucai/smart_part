package cn.lps.controller;
import cn.lps.model.base.PushReq;
import cn.lps.model.request.CarArriveReq;
import cn.lps.service.impl.LpsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/gateway/api")
@Slf4j
public class LpsGatewayController {

    @Autowired
    private LpsManager lpsManager;

    /**
     * @Description: 车皮到站确认接口
     * @Author: liuyucai
     **/
    @RequestMapping(value = "/saveCarArrive", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> saveCarArrive(@RequestBody PushReq pushReq) {
        return lpsManager.saveCarArrive(pushReq);
    }

    /**
     * @Description: 车辆放行接口
     * @Author: liuyucai
     **/
    @RequestMapping(value = "/saveCarLeave", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> saveCarLeave(@RequestBody PushReq pushReq) {
        return lpsManager.saveCarLeave(pushReq);
    }

    /**
     * @Description: 业务数据推送接口
     * @Author: liuyucai
     **/
    @RequestMapping(value = "/saveBusinessData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> saveBusinessData(@RequestBody PushReq pushReq) {
        return lpsManager.saveBusinessData(pushReq);
    }
}
