package org.smartx.summer.bean;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> 错误返回 </p>
 *
 * <b>Creation Time:</b> 2016年10月26日
 *
 * @author Ming
 * @since summer 0.1
 */
public class State implements Serializable {

    private Integer code;


    private String msg;

    public State() {
    }


    public State(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static State buildFromBindingResult(BindingResult bindingResult) {
        StringBuilder stringBuilder = new StringBuilder();
        bindingResult.getAllErrors().forEach(b -> {
            stringBuilder.append(b.getDefaultMessage()).append("\n");
        });
        return new State(HttpStatus.BAD_REQUEST.value() * 10, stringBuilder.toString());
    }
}
