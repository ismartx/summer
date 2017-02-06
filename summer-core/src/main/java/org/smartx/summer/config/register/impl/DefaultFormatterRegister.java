package org.smartx.summer.config.register.impl;

import org.smartx.summer.config.register.FormatterRegister;
import org.smartx.summer.formatter.DefaultRangeHeaderFormatter;
import org.smartx.summer.formatter.DefaultXUserAgentHeaderFormatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;

/**
 * <p> 默认formatter注册器 </p>
 *
 * <b>Creation Time:</b> 2016年10月28日
 *
 * @author binglin
 * @since summer 0.1
 */
@Component
public class DefaultFormatterRegister implements FormatterRegister {
    @Override
    public void registry(FormatterRegistry registry) {
        registry.addFormatter(new DefaultRangeHeaderFormatter());
        registry.addFormatter(new DefaultXUserAgentHeaderFormatter());
    }
}
