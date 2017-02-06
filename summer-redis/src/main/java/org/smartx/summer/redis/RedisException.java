package org.smartx.summer.redis;

/**
 * <p> Redis exception </p>
 *
 * <b>Creation Time:</b> 2016年10月21日
 *
 * @author kext
 * @since summer 0.1
 */
public class RedisException extends Exception {
    private static final long serialVersionUID = 7115322404472565037L;

    public RedisException() {
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisException(String message) {
        super(message);
    }

    public RedisException(Throwable cause) {
        super(cause);
    }
}
