package cn.lps.model.base;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 请求基础类
 */
@Data
@ToString
public class BaseReq implements Serializable {
    /**
     * 请求日志号
     */
    @NotBlank(message = "请求日志号不能为空")
    private String reqLog;

    /** 登录账号 */
    private String loginAccount;

    /** 机构ID */
    private Integer loginStationId;

    /** 登录用户id */
    private Integer loginUserId;

    /**用户姓名*/
    private String loginRealName;

    private transient String logType;
}
