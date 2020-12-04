package cn.lps.model.request;

import cn.lps.model.base.BaseReq;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
public class BusinessDataReq extends BaseReq {


    private static final long serialVersionUID = -5766392746185821015L;

    private Integer businessDataId;

    /*
     *  仓库名称
     */
    private String storehouseName;

    /*
     *  业务类型
     */
    private String businessType;

    /*
     *  总重量
     */
    private Double totalWeight;

    /*
     *  总成交量
     */
    private Double totalVolume;

    /*
     *  总交易金额
     */
    private Double totalMoney;


    private Date createTime;

    private Date updateTime;

}
