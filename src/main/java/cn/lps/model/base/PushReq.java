package cn.lps.model.base;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
public class PushReq extends BaseReq {
    private static final long serialVersionUID = -1939226781777967497L;

    /**条件*/
    private String pushParam;
    /**密钥key*/
    private String pushKey;
    /**账号*/
    private String username;
    /**账号*/
    private String passwd;
}
