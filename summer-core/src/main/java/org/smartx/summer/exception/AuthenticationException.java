package org.smartx.summer.exception;

/**
 * <p> 身份验证异常 </p>
 *
 * <b>Creation Time:</b> 2016年10月26日
 *
 * @author Ming
 * @since summer 0.1
 */
public class AuthenticationException extends RuntimeException {
    private static final long serialVersionUID = -8799659324455306881L;

    public AuthenticationException(String message) {
        super(message);
    }
}
