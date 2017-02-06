package org.smartx.summer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

/**
 * <p> 拦截 每次请求的 url 、请求的数据、header,response </p>
 *
 * <b>Creation Time:</b> 2016年11月04日
 *
 * @author binglin
 * @since summer 0.1
 */
@Component
public class ServletRequestHandledEventListener implements ApplicationListener<ServletRequestHandledEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ServletRequestHandledEventListener.class);

    @Override
    public void onApplicationEvent(ServletRequestHandledEvent servletRequestHandledEvent) {
        logger.info(String.format("total request processing time: %d ms", servletRequestHandledEvent.getProcessingTimeMillis()));
    }
}
