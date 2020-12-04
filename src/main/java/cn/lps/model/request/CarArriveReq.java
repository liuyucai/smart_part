package cn.lps.model.request;

import cn.lps.model.base.BaseReq;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
public class CarArriveReq extends BaseReq {
    private static final long serialVersionUID = -8895923276621949456L;

    private Integer carArriveId;

    private String carNumber;

    private Date arriveTime;

    private Date createTime;

    private Date updateTime;
}
