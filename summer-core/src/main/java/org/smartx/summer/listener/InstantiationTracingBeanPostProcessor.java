package org.smartx.summer.listener;

import org.smartx.summer.common.SpringMvcContextHolder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * <p> 获取spring mvc context </p>
 *
 * <b>Creation Time:</b> 2016年10月21日
 *
 * @author binglin
 * @since summer 0.1
 */

@Component
public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (contextRefreshedEvent.getApplicationContext().getParent() != null) {
            SpringMvcContextHolder.setApplicationContext(contextRefreshedEvent.getApplicationContext());
        }
    }
}
