package org.smartx.summer.config.register;

import org.springframework.format.FormatterRegistry;

/**
 * <p> Formatter注册器 </p>
 *
 * <b>Creation Time:</b> 2016年10月28日
 *
 * @author binglin
 * @since summer 0.1
 */
public interface FormatterRegister {
    void registry(FormatterRegistry registry);
}
