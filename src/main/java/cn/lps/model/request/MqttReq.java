package cn.lps.model.request;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author liuyucai
 * @Created 2020/8/13
 * @Description
 */
@Data
@ToString(callSuper = true)
public class MqttReq implements Serializable {
    private static final long serialVersionUID = 8356066622614660223L;

    private String body;

    private String topic;
}
