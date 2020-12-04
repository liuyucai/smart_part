package cn.lps.controller;

import cn.lps.model.request.MqttReq;
import cn.lps.service.manage.MqttPushClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liuyucai
 * @Created 2020/8/14
 * @Description
 */
@Controller
public class MqttController {

    private String host="tcp://127.0.0.1:1883";

    @Value("${name}")
    private String username;

    @Value("${password}")
    private String password;

//    @Value("${clientid}")
    private String clientid;

    private int timeout =1000;
    private int keepalive =1000;

    @Autowired
    private MqttPushClient mqttPushClient;

    @RequestMapping("/public")
    @ResponseBody
    public String mqttPublic(MqttReq mqttReq){
//        String kdTopic = "topic2";
//        String pushMessage = "{'name':'刘宇才','sex':'男'}";
        clientid = String.valueOf(System.currentTimeMillis());
        mqttPushClient.connect(host, clientid, username, password, timeout,keepalive);
        mqttPushClient.publish(0, false, mqttReq.getTopic(),mqttReq.getBody());
//        mqttPushClient.subscribe(kdTopic);
        return "123";
    }

    @RequestMapping("/subscribe")
    @ResponseBody
    public String mqttSubscribe(MqttReq mqttReq){
//        String kdTopic = "topic2";
        clientid = String.valueOf(System.currentTimeMillis());
        mqttPushClient.connect(host, clientid, username, password, timeout,keepalive);
        mqttPushClient.subscribe(mqttReq.getTopic());
        return "12345";
    }
}