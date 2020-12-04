package cn.lps.controller;

import cn.lps.model.request.BusinessDataReq;
import cn.lps.model.request.CarArriveReq;
import cn.lps.model.request.CarLeaveReq;
import cn.lps.service.impl.LpsInterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/lps")
public class LpsController {

    @Autowired
    private LpsInterfaceService lpsInterfaceService;

    @RequestMapping(value = "/carArrive", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> carArrive(CarArriveReq carArriveReq) {
        Map <String, Object> map = lpsInterfaceService.savePush(carArriveReq);
        return map;
    }

    @RequestMapping(value = "/carLeave", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> carLeave(CarLeaveReq carLeaveReq) {
        Map <String, Object> map = lpsInterfaceService.savePushCarLeave(carLeaveReq);
        return map;
    }

    @RequestMapping(value = "/businessData", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> businessData(BusinessDataReq businessDataReq) {
        Map <String, Object> map = lpsInterfaceService.saveBusinessData(businessDataReq);
        return map;
    }

    @RequestMapping(value = "/threadTest", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> threadTest() throws InterruptedException {
        Map <String, Object> map = new HashMap<>();
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(6000);
        System.out.println("111");
        map.put("线程","111");
        return map;
    }
}
