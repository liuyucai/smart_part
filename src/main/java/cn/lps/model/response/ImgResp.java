package cn.lps.model.response;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author liuyucai
 * @Created 2020/11/5
 * @Description
 */
@Data
@ToString(callSuper = true)
public class ImgResp implements Serializable {
    private static final long serialVersionUID = -138609243335461875L;

    private String bigImg;

    private String slidImg;
}
