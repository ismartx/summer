package org.smartx.summer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartx.summer.config.register.FormatterRegister;
import org.smartx.summer.config.register.MessageConvertersRegister;
import org.smartx.summer.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by binglin on 2016/10/27.
 *
 * 代替spring-mvc.xml文件
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.smartx.summer.controller",
        "org.smartx.summer.interceptor",
        "org.smartx.summer.config.register",
        "org.smartx.summer.register",
        "org.smartx.summer.filter",
        "org.smartx.summer.session",
        "org.smartx.summer.config",
        "org.smartx.summer.listener"})
public class WebConfig extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfigurerAdapter.class);


    @Autowired(required = false)
    private MessageConvertersRegister messageConvertersRegister;

    @Autowired(required = false)
    private FormatterRegister formatterRegistry;

    @Autowired
    private JwtInterceptor jwtInterceptor;

//    public WebConfig(MessageConvertersRegister messageConvertersRegister, FormatterRegister formatterRegistry) {
//        this.messageConvertersRegister = messageConvertersRegister;
//        this.formatterRegistry = formatterRegistry;
//    }

    /**
     * 注册http converter
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        if (messageConvertersRegister != null) {
            messageConvertersRegister.registry(converters);
        }
    }

    /**
     * 注册Formatter，可以在controller中直接使用@RequestHeader("Range") PageAble Range
     * 直接从header中获取字符串并自动转换为相应的对象
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        if (formatterRegistry != null) {
            formatterRegistry.registry(registry);
        }
    }

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor);
    }

    /**
     * 添加对文件上传的支持
     */
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(10485760000L);
        multipartResolver.setMaxInMemorySize(40960);
        multipartResolver.setDefaultEncoding("utf-8");
        return multipartResolver;
    }

    /**
     * 添加swagger的静态资源的处理
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
