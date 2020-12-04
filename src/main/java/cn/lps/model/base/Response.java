package cn.lps.model.base;

import lombok.ToString;

import java.io.Serializable;

/**
 * @version V1.0
 * @ProjectName imsp-parent
 * @Author Qiang Yan
 * @Description 统一的Response
 * @Date Created In 2017/12/5 15:20
 * @Modified
 */
@ToString
public class Response<T> implements Serializable {
    private boolean success;  //表示调用是否成功 ,如果为true,则可以调用getResult,如果为false,则调用errorCode来获取出错信息

    private T result;  //获取调用返回值

    private String errorCode;

    private String errorMsg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        success = true;
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
