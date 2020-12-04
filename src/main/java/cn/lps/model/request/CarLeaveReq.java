package cn.lps.model.request;

import cn.lps.model.base.BaseReq;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
public class CarLeaveReq extends BaseReq {

    private static final long serialVersionUID = -7183760866131629836L;

    private Integer carLeaveId;

    private String carNumber;

    private Date leaveTime;

    private String allowLeave;

    private Date createTime;

    private Date updateTime;

}
