package cn.lps.controller;

import cn.lps.model.request.CarArriveReq;
import cn.lps.model.request.ImgReq;
import cn.lps.model.response.ImgResp;
import cn.lps.utils.VerifyImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyucai
 * @Created 2020/11/5
 * @Description
 */
@Slf4j
@Controller
@RequestMapping("/imageAuthentication")
public class ImgController {

    @RequestMapping(value = "/getImage", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getImage(ImgReq imgReq) throws IOException {
        Map <String, Object> map = imageHelder(imgReq);
        return map;
    }

    private Map <String, Object> imageHelder(ImgReq imgReq) throws IOException {
        Map <String, Object> map = new HashMap<>();
        map.put("status","success");
        ImgResp imgResp = new ImgResp();
        imgResp.setBigImg("bigImg111");
        imgResp.setSlidImg("slidImg222");
        map.put("data",imgResp);
        map = VerifyImageUtil.getVerifyImage();
        return map;
    }

}
