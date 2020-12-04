package cn.lps.model.request;

import cn.lps.model.base.BaseReq;
import lombok.Data;
import lombok.ToString;

/**
 * @author liuyucai
 * @Created 2020/11/5
 * @Description
 */
@Data
@ToString(callSuper = true)
public class ImgReq extends BaseReq {
    private static final long serialVersionUID = -5450929472393715813L;

    private Integer width;

    private Integer height;
}
