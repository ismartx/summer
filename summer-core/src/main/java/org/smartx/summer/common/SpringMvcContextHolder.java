package org.smartx.summer.common;

import org.springframework.context.ApplicationContext;

/**
 * <p> 以静态变量保存Spring mvc ApplicationContext, 可在任何代码任何地方任何时候中取出ApplicationContext </p>
 *
 * <b>Creation Time:</b> 2015年3月18日
 *
 * @author kext
 * @since summer 0.1
 */
public final class SpringMvcContextHolder {

    private static ApplicationContext applicationContext = null;

    /**
     * 实现ApplicationContextAware接口, 注入Context到静态变量中.
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringMvcContextHolder.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> clazz) {
        assertContextInjected();
        return applicationContext.getBean(clazz);
    }

    /**
     * 检查ApplicationContext不为空.
     */
    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new IllegalStateException(
                    "applicationContext未注入,请在spring配置文件中定义SpringContextHolder");
        }
    }
}
