package cn.lps.dao;

import cn.lps.model.request.BusinessDataReq;
import cn.lps.model.request.CarArriveReq;
import cn.lps.model.request.CarLeaveReq;

public interface LpsMapper {

   int saveCarArrive(CarArriveReq paPageReq);

    int saveCarLeave(CarLeaveReq carLeaveReq);

    int saveBusinessData(BusinessDataReq businessDataReq);
}
