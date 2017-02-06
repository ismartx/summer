package org.smartx.summer.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartx.summer.bean.State;
import org.smartx.summer.exception.AuthenticationException;
import org.smartx.summer.exception.BaseServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * <p> 统一的异常拦截处理 所有错误码均为相应的http错误码乘10得到 </p>
 *
 * <b>Creation Time:</b> 2016年10月24日
 *
 * @author Ming
 * @since summer 0.1
 */
@ControllerAdvice
public class WebAppExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(WebAppExceptionAdvice.class);

    @ExceptionHandler({Exception.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public State exceptionHandler(Exception ex) {
        logger.error("Exception:{}", ex);
        return new State(HttpStatus.INTERNAL_SERVER_ERROR.value() * 10, ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public State handleAuthenticationException(AuthenticationException ex) {
        logger.error("AuthenticationException:{}", ex);
        return new State(HttpStatus.UNAUTHORIZED.value() * 10, ex.getMessage());
    }

    // 使用@Valid 注解时校验数据，数据不合法时会抛出此异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public State handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.error("MethodArgumentNotValidException:{}", ex);
        return State.buildFromBindingResult(ex.getBindingResult());
    }

    //ServletRequestBindingException
    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public State handleServletRequestBindingException(ServletRequestBindingException ex) {
        logger.error("MethodArgumentNotValidException:{}", ex);
        return new State(HttpStatus.BAD_REQUEST.value() * 10, ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public State handlerNotFoundException(HttpServletRequest req, NoHandlerFoundException ex) {
        logger.error("NoHandlerFoundException:{}", ex);
        String errorURL = req.getRequestURL().toString();
        return new State(HttpStatus.NOT_FOUND.value() * 10, "Not found this url:".concat(errorURL));
    }

    @ExceptionHandler(BaseServiceException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public State handlerBaseServiceException(BaseServiceException ex) {
        logger.error("BaseServiceException:{}", ex);
        return new State(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public State handleConstraintViolationException(ConstraintViolationException ex) {
        logger.error("ConstraintViolationException:{}", ex);
        return new State(HttpStatus.BAD_REQUEST.value() * 10, ex.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public State handleSqlException(SQLException ex) {
        logger.error("SQLException:{}", ex);
        return new State(HttpStatus.INTERNAL_SERVER_ERROR.value() * 10, "SQL Exception");
    }
}
