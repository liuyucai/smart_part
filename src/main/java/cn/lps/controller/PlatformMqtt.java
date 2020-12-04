package cn.lps.controller;

import cn.lps.model.request.MqttReq;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Map;


/**
 * @author liuyucai
 * @Created 2020/8/13
 * @Description
 */
@Controller
@RequestMapping("/mqtt")
@Component
public class PlatformMqtt implements MqttCallback {


//    @Value("${host}")
    private String HOST="tcp://127.0.0.1:1883";
    @Value("${name}")
    private String name;
    @Value("${password}")
    private String passWord;

    private MqttClient client;
    private MqttMessage message;
//    private final static Logger log = LoggerFactory.getLogger(PlatformMqtt.class);
    String  clientid= String.valueOf(System.currentTimeMillis());
    @PostConstruct
    public void send() {
        try {
            client = new MqttClient(HOST, clientid, new MemoryPersistence());

        } catch (MqttException e) {
            e.printStackTrace();
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(name);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(3600);
        try {
            client.setCallback(this);
            client.connect(options);
        } catch (Exception e) {
//            log.info("platform-Mqtt客户端连接异常，异常信息："+e);
        }

    }

    @RequestMapping("/request")
    @ResponseBody
    public String publish(MqttReq mqttReq) throws MqttException {
//        String body= String.valueOf(parameter.getRequestBody().get("body"));
//        String topic= String.valueOf(parameter.getRequestBody().get("topic"));
        String body= "mqtt1231";
        String topic= "mqtt_test";
        topic="DataTopic/"+topic;
//        log.info("请求主题为："+topic+",消息为："+body);
        message = new MqttMessage();
        message.setQos(0);
        message.setRetained(false);
        message.setPayload(body.getBytes());
        client.publish(topic,message);
        return "123";
    }

    @Override
    public void connectionLost(Throwable throwable) {
//        log.info("程序出现异常，正在重新连接...:");
        try {
            client.close();
            System.out.println("开始发布消息，000");
            send();
        } catch (MqttException e) {
//            log.info(e.getMessage());
        }
//        log.info("platform-Mqtt重新连接成功");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        System.out.println("消息发送到达，1111");

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//        log.info("消息发送成功");
        System.out.println("消息发送成功，222");
    }
}