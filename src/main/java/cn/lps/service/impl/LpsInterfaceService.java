package cn.lps.service.impl;

import cn.lps.model.base.PushReq;
import cn.lps.model.request.BusinessDataReq;
import cn.lps.model.request.CarArriveReq;
import cn.lps.model.request.CarLeaveReq;
import cn.lps.utils.AESUtils;
import cn.lps.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@PropertySource(value = {"classpath:lps.properties"})
public class LpsInterfaceService {

    @Value("${lps_car_arrive_uri}")
    private String carArriveUri;

    @Value("${lps_car_leave_uri}")
    private String carLeaveUri;

    @Value("${lps_business_data_uri}")
    private String businessDataUri;

    @Value("${lps_username}")
    private String envUsername;
    @Value("${lps_password}")
    private String envPasswd;

    @Autowired
    LpsHttpServiceRemote lpsHttpServiceRemote;

    public Map<String, Object> savePush(CarArriveReq carArriveReq) {

        PushReq pushReq = new PushReq();

        carArriveReq.setArriveTime(new Date());
        String param = JSON.toJSONString(carArriveReq);
        try {
            //set加密后的参数
            setAESPushReq(pushReq,param);
            String result = lpsHttpServiceRemote.remote(JSON.toJSONString(pushReq), carArriveUri);
            if (!StringUtils.isEmpty(result)) {
                return JSON.parseObject(result, HashMap.class);
            }
        } catch (Exception e) {
            log.error("SMIS_PUSH:同步省系统失败！", e);
        }
        return null;
    }

    public Map<String, Object> savePushCarLeave(CarLeaveReq carLeaveReq) {
        PushReq pushReq = new PushReq();

        carLeaveReq.setLeaveTime(new Date());
        String param = JSON.toJSONString(carLeaveReq);
        try {
            //set加密后的参数
            setAESPushReq(pushReq,param);
            String result = lpsHttpServiceRemote.remote(JSON.toJSONString(pushReq), carLeaveUri);
            if (!StringUtils.isEmpty(result)) {
                return JSON.parseObject(result, HashMap.class);
            }
        } catch (Exception e) {
            log.error("SMIS_PUSH:同步省系统失败！", e);
        }
        return null;
    }

    public Map<String, Object> saveBusinessData(BusinessDataReq businessDataReq) {
        PushReq pushReq = new PushReq();

        businessDataReq.setStorehouseName("天讯仓库");
        String param = JSON.toJSONString(businessDataReq);
        try {
            //set加密后的参数
            setAESPushReq(pushReq,param);
            String result = lpsHttpServiceRemote.remote(JSON.toJSONString(pushReq), businessDataUri);
            if (!StringUtils.isEmpty(result)) {
                return JSON.parseObject(result, HashMap.class);
            }
        } catch (Exception e) {
            log.error("SMIS_PUSH:同步省系统失败！", e);
        }
        return null;
    }

    /**
     * @Description: pushReq加密参数set
     * @Author: Howie
     * @Date: 2019/11/28 14:13
     **/
    private void setAESPushReq(PushReq pushReq,String param) {
        //生成密钥
        String secretKey = AESUtils.getKey();
        //加密
        String aesKey = AESUtils.encryptAESKey(secretKey, AESUtils.AES_KEY_ENCRYPT_PUBLIC_KEY);
        pushReq.setPushKey(aesKey);

        //账号
        String username = AESUtils.encrypt(envUsername, secretKey);
        pushReq.setUsername(username);

        //密码
        String passwd = AESUtils.encrypt(envPasswd, secretKey);
        pushReq.setPasswd(passwd);

        log.info("加密前参数：" + param);
        param = AESUtils.encrypt(param, secretKey);
        pushReq.setPushParam(param);
        log.info("param：" + param);
    }
}
