package cn.lps.service;


import cn.lps.model.base.Response;
import cn.lps.model.request.BusinessDataReq;
import cn.lps.model.request.CarArriveReq;
import cn.lps.model.request.CarLeaveReq;

public interface LpsService {
    Response<Boolean> saveCarArrive(CarArriveReq paPageReq);

    Response<Boolean> saveCarLeave(CarLeaveReq carLeaveReq);

    Response<Boolean> saveBusinessData(BusinessDataReq businessDataReq);
}
