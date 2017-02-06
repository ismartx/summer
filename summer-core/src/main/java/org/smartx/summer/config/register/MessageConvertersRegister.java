package org.smartx.summer.config.register;

import org.springframework.http.converter.HttpMessageConverter;

import java.util.List;

/**
 * <p> 消息converter注册器 </p>
 *
 * <b>Creation Time:</b> 2016年10月28日
 *
 * @author binglin
 * @since summer 0.1
 */
public interface MessageConvertersRegister {

    void registry(List<HttpMessageConverter<?>> converters);
}
