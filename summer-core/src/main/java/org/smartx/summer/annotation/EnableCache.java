package org.smartx.summer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 开启cache特性的注解, 详见{@link org.smartx.summer.interceptor.CacheAdvice} </p>
 *
 * <b>Creation Time:</b> 2016年10月21日
 *
 * @author binglin
 * @since summer 0.1
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableCache {
}
