package cn.lps.model.request;

import cn.lps.model.base.BaseReq;
import lombok.Data;
import lombok.ToString;

/**
 * @author liuyucai
 * @Created 2020/10/19
 * @Description
 */
@Data
@ToString
public class UserReq extends BaseReq {
    private static final long serialVersionUID = 6539420589049032989L;

    private Integer userId;

    private String userAccount;

    private String userName;

    private String password;
}
