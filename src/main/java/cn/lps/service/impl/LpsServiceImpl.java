package cn.lps.service.impl;

import cn.lps.dao.LpsMapper;
import cn.lps.model.base.Response;
import cn.lps.model.request.BusinessDataReq;
import cn.lps.model.request.CarArriveReq;
import cn.lps.model.request.CarLeaveReq;
import cn.lps.service.LpsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LpsServiceImpl implements LpsService {
    @Autowired
    private LpsMapper lpsMapper;

    @Override
    public Response<Boolean> saveCarArrive(CarArriveReq paPageReq) {
        Response<Boolean> response = new Response<>();
        int  num = lpsMapper.saveCarArrive(paPageReq);
        if (num>0){
            response.setResult(true);
        }else{
            response.setResult(false);
        }
        return response;
    }

    @Override
    public Response<Boolean> saveCarLeave(CarLeaveReq carLeaveReq) {
        Response<Boolean> response = new Response<>();
        int  num = lpsMapper.saveCarLeave(carLeaveReq);
        if (num>0){
            response.setResult(true);
        }else{
            response.setResult(false);
        }
        return response;
    }

    @Override
    public Response<Boolean> saveBusinessData(BusinessDataReq businessDataReq) {
        Response<Boolean> response = new Response<>();
        int  num = lpsMapper.saveBusinessData(businessDataReq);
        if (num>0){
            response.setResult(true);
        }else{
            response.setResult(false);
        }
        return response;
    }
}
