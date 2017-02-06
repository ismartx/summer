package org.smartx.summer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> Jwt 校验注解, 详见{@link org.smartx.summer.interceptor.JwtInterceptor} </p>
 *
 * <b>Creation Time:</b> 2016年10月25日
 *
 * @author Ming
 * @since summer 0.1
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyJwtRole {
    String[] roles() default {};

    //是否需要判断 token 的id  和 redis 中的一致
    //如果方法没有经过 JwtTokenAuthFilter ,务必将此设为 true
    boolean verifyJwt() default false;
}
