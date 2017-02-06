package org.smartx.summer.exception;

import org.smartx.summer.bean.State;

/**
 * <p> 服务异常 </p>
 *
 * <b>Creation Time:</b> 2016年10月28日
 *
 * @author binglin
 * @since summer 0.1
 */
public class BaseServiceException extends Exception {
    private Integer errorCode;

    private String message;

    public BaseServiceException(Integer errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public BaseServiceException() {
        super();
    }

    public BaseServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseServiceException(State state) {
        super();
        this.errorCode = state.getCode();
        this.message = state.getMsg();
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
