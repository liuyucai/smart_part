package cn.lps.service.impl;

import cn.lps.model.base.PushReq;
import cn.lps.model.base.Response;
import cn.lps.model.request.BusinessDataReq;
import cn.lps.model.request.CarArriveReq;
import cn.lps.model.request.CarLeaveReq;
import cn.lps.service.LpsService;
import cn.lps.utils.AESUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class LpsManager {
//    private static String privateKeyStr = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ11NRdNPNsm6ANneWdS44eTumWn38kR" +
//            "/ReqPKUGYdyU0uD3YJaHtuynRAyF1zR+mzFoT+mxYC3FIytrjGOyIy8/580LKkWVbkww9eVjXZSRDWzjvWNVdK8viSHDiBLM/LZxsBS3jy8" +
//            "t1lv4zlHHohzFpzJM2mm9LfhrCVLfkjd9AgMBAAECgYBj1ZnY8WcBiPSYm/X01jBfmQIZTExuv5IafBzBgX9xDYd7jj3Wk6we9psF2aKurQuXUw" +
//            "1AHe/edV0sPZ+g4qS9ZMuJa1PNFcdeXAUicImUxIAyWd/gnpqn1vTZQXx4ymKF55VY3FV+JiDbM1wb1MV20kSFbGPPi/63q7izAZRxPQJBANICQHiFr3znj" +
//            "4ysRUCq9id1B944kfzq0Eti54B/b6AepvBQk/ctK8p7sZTiFGEsauyo/H7W0C+kPKq2YB0BqB8CQQC/8Mj8jU7pB6UOR/8J41YqCNL3hSKUS/Oq93Wn98kwNfej" +
//            "QxlQQpo7S+u2R1BaA1pHkjtLaOCxYtk0EJeHcVzjAkBS45aku0c/inoLMPeIhbHwcu2vFS7x35BlIN10x1e8oDyNv5AXUFnnapj1xaH7lLeDP1OhkJHNLArR6nfXG" +
//            "w9LAkAMfbYGwYd2INo8ALF3SkUsPSDFnPNwJTU5VhthD/4W1hxEkrROBdeVrk4rsZ5oDTnN2JVlRfEBekZaXg4OcXEzAkAJWXBCxfpc6ToqA1zZ/twhjZea8v2CzrSvGp" +
//            "vwOYGE7ycIe9N5LcUmc1moF+YM7U/zYH+SpVHMGgzXsCO9PCcx";
//    private static String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCddTUXTTzbJugDZ3lnUu" +
//            "OHk7plp9/JEf0XqjylBmHclNLg92CWh7bsp0QMhdc0fpsxaE/psWAtxSMra4xjsiMvP+fNCypFlW5MMPXlY12UkQ1s471jVXSvL4khw4gSzPy2cbAUt48vLdZb+M5Rx6IcxacyTNppvS34awlS35I3fQIDAQAB";

    @Value("${privateKeyStr}")
    private String privateKeyStr;

    @Value("${publicKeyStr}")
    private String publicKeyStr;

    @Value("${lps_username}")
    private String envUsername;
    @Value("${lps_password}")
    private String envPasswd;

    @Autowired
    private LpsService lpsService;


    public Map<String, Object> saveCarArrive(PushReq pushReq) {
        try {
            Map<String, Object> map = lpsHandler(pushReq);

            if(map.get("statusCode").equals("0")){
                String param = (String) map.get("param");
                CarArriveReq carArriveReq = JSONObject.parseObject(param, CarArriveReq.class);

                if (carArriveReq == null) {
                    return setResultMap("101", "请输入需要增加内容!");
                }
                Response<Boolean> response = lpsService.saveCarArrive(carArriveReq);

                //加密封装返回结果
                if (response != null && response.isSuccess()) {
                    return setResultMap("0", "添加成功!");
                }

            }else{
                return map;
            }
        } catch (Exception e) {
            log.error("车皮到站数据添加失败", e);
        }
        return setResultMap("999", "添加错误!");
    }

    public Map<String, Object> saveCarLeave(PushReq pushReq) {
        try {
            Map<String, Object> map = lpsHandler(pushReq);

            if(map.get("statusCode").equals("0")){
                String param = (String) map.get("param");
                CarLeaveReq carLeaveReq = JSONObject.parseObject(param, CarLeaveReq.class);

                if (carLeaveReq == null) {
                    return setResultMap("101", "请输入需要增加内容!");
                }
                Response<Boolean> response = lpsService.saveCarLeave(carLeaveReq);

                //加密封装返回结果
                if (response != null && response.isSuccess()) {
                    return setResultMap("0", "添加成功!");
                }

            }else{
                return map;
            }
        } catch (Exception e) {
            log.error("车皮到站数据添加失败", e);
        }
        return setResultMap("999", "添加错误!");
    }

    public Map<String, Object> saveBusinessData(PushReq pushReq) {
        try {
            Map<String, Object> map = lpsHandler(pushReq);

            if(map.get("statusCode").equals("0")){
                String param = (String) map.get("param");
                BusinessDataReq businessDataReq = JSONObject.parseObject(param, BusinessDataReq.class);

                if (businessDataReq == null) {
                    return setResultMap("101", "请输入需要增加内容!");
                }
                Response<Boolean> response = lpsService.saveBusinessData(businessDataReq);

                //加密封装返回结果
                if (response != null && response.isSuccess()) {
                    return setResultMap("0", "添加成功!");
                }

            }else{
                return map;
            }
        } catch (Exception e) {
            log.error("车皮到站数据添加失败", e);
        }
        return setResultMap("999", "添加错误!");
    }

    public Map<String, Object> setResultMap(String statusCode, String msg) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("statusCode", statusCode);
        resultMap.put("msg", msg);
        return resultMap;
    }

    public Map<String, Object> lpsHandler(PushReq pushReq){
        Map<String, Object> resultMap = new HashMap<>();

        //获得密钥及封装好得参数
        String pushKey = pushReq.getPushKey();
        String pushParam = pushReq.getPushParam();
        String username = pushReq.getUsername();
        String passwd = pushReq.getPasswd();

        if (StringUtils.isEmpty(pushKey) || StringUtils.isEmpty(username) || StringUtils.isEmpty(passwd) || StringUtils.isEmpty(pushParam)) {
            return setResultMap("101", "请输入需要访问条件!");
        }

        pushKey = pushKey.replaceAll(" ", "+");
        pushParam = pushParam.replaceAll(" ", "+");
        username = username.replaceAll(" ", "+");
        passwd = passwd.replaceAll(" ", "+");


        //解密参数
        String key = AESUtils.decryptAESKey(pushKey, privateKeyStr);
        String param = AESUtils.decrypt(pushParam, key);

        //账号
        username = AESUtils.decrypt(username, key);
        //密码
        passwd = AESUtils.decrypt(passwd,key);

        if(!(username.equals(envUsername) && passwd.equals(envPasswd))){
            return setResultMap("101", "用户名、密码错误!");
        }

        resultMap.put("statusCode", "0");
        resultMap.put("param", param);
        return resultMap;
    }
}
